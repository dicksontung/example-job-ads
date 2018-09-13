package com.dickson.job.ads

import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.EntityNotFoundException

@RestController
class CheckoutController(val checkoutRepository: CheckoutRepository,
                         val productRepository: ProductRepository,
                         val itemRepository: ItemRepository,
                         val userRepository: UserRepository) {

    @PostMapping("/api/checkout")
    fun newCheckout(@RequestBody checkout: Checkout): Checkout = checkoutRepository.save(checkout)

    @GetMapping("/api/checkout/{id}")
    fun findAll(@PathVariable id: Long): Checkout = checkoutRepository.findById(id).get()

    @PostMapping("/api/checkout/{id}/items")
    fun newItem(@PathVariable id: Long, @RequestBody item: Item): Checkout {
        val product = productRepository.findById(item.product.id)
        val checkout = checkoutRepository.findById(id)
        if (product.isPresent && checkout.isPresent) {
            val newItem = itemRepository.save(Item(name = item.name, product = product.get()))
            val co = checkout.get().add(newItem)
            co.total = recalculate(co)
            return checkoutRepository.save(co)
        }
        throw EntityNotFoundException()
    }

    @DeleteMapping("/api/checkout/{id}/items/{itemId}")
    fun deleteItem(@PathVariable id: Long, @PathVariable itemId: Long): Checkout {
        val co = checkoutRepository.findById(id).get()
        co.remove(itemId)
        co.total = recalculate(co)
        return checkoutRepository.save(co)
    }

    fun recalculate(checkout: Checkout): BigDecimal {
        val user = userRepository.findById(checkout.userId)
        val total = if (user.isPresent && user.get().promotion.isApplicable(checkout)) {
            user.get().promotion.calcPrice.invoke(checkout)
        } else {
            Promotion.NOT_AVAILABLE.calcPrice.invoke(checkout)
        }

        return total.setScale(2, RoundingMode.HALF_UP)
    }
}
