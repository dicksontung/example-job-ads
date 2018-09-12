package com.dickson.job.ads

import org.springframework.web.bind.annotation.*
import java.math.RoundingMode
import javax.persistence.EntityNotFoundException

@RestController
class CheckoutController(val checkoutRepository: CheckoutRepository,
                         val productRepository: ProductRepository,
                         val itemRepository: ItemRepository,
                         val userRepository: UserRepository) {

    @PostMapping("/checkout")
    fun newCheckout(@RequestBody checkout: Checkout): Checkout = checkoutRepository.save(checkout)

    @GetMapping("/checkout/{id}")
    fun findAll(@PathVariable id: Long): Checkout = checkoutRepository.findById(id).get()

    @PostMapping("checkout/{id}/items")
    fun newItem(@PathVariable id: Long, @RequestBody item: Item): Checkout {
        val product = productRepository.findById(item.product.id)
        val checkout = checkoutRepository.findById(id)

        if (product.isPresent && checkout.isPresent) {
            val newItem = itemRepository.save(Item(name = item.name, product = product.get()))
            var co = checkout.get().add(newItem)
            val user = userRepository.findById(co.userId)
            val total = if (user.isPresent && user.get().promotion.isApplicable(co)) {
                user.get().promotion.calcPrice.invoke(co)
            } else {
                Promotion.NOT_AVAILABLE.calcPrice.invoke(co)
            }
            co.total = total.setScale(2, RoundingMode.HALF_UP)
            return checkoutRepository.save(co)
        }


        throw EntityNotFoundException()
    }
}
