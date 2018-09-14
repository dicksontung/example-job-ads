package com.dickson.job.ads

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

class FordTest {

    @Test
    fun `test Ford promotion`() {
        var checkout = Checkout(
                1, 1, mutableListOf(
                Item(product = Product(
                        "Classic", "Classic", BigDecimal.TEN
                )),
                Item(product = Product(
                        "Classic", "Classic", BigDecimal.TEN
                )),
                Item(product = Product(
                        "Classic", "Classic", BigDecimal.TEN
                )),
                Item(product = Product(
                        "Classic", "Classic", BigDecimal.TEN
                ))
        ))
        assertThat(Promotion.FORD.calcPrice(checkout)).isEqualTo(BigDecimal(40).setScale(2))
        checkout.items.add(
                Item(product = Product(
                        "Classic", "Classic", BigDecimal.TEN
                ))
        )
        assertThat(Promotion.FORD.calcPrice(checkout)).isEqualTo(BigDecimal(40).setScale(2))
        checkout.items.add(
                Item(product = Product(
                        "Classic", "Classic", BigDecimal.TEN
                ))
        )
        assertThat(Promotion.FORD.calcPrice(checkout)).isEqualTo(BigDecimal(50).setScale(2))
        checkout.items.add(
                Item(product = Product(
                        "Standout", "Standout", BigDecimal.ONE
                ))
        )
        assertThat(Promotion.FORD.calcPrice(checkout)).isEqualTo(BigDecimal(359.99).setScale(2, RoundingMode.HALF_UP))
        checkout.items.addAll(
                listOf(
                        Item(product = Product(
                                "Premium", "Premium", BigDecimal.ONE
                        )),
                        Item(product = Product(
                                "Premium", "Premium", BigDecimal.ONE
                        ))
                        )
        )
        assertThat(Promotion.FORD.calcPrice(checkout)).isEqualTo(BigDecimal(361.99).setScale(2, RoundingMode.HALF_UP))
        checkout.items.addAll(
                listOf(
                        Item(product = Product(
                                "Premium", "Premium", BigDecimal.ONE
                        )),
                        Item(product = Product(
                                "Premium", "Premium", BigDecimal.ONE
                        ))
                )
        )
        assertThat(Promotion.FORD.calcPrice(checkout)).isEqualTo(BigDecimal(1919.95).setScale(2, RoundingMode.HALF_UP))
    }

}