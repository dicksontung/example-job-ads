package com.dickson.job.ads

import java.math.BigDecimal

enum class Promotion(val description: String,
                     val calcPrice: (checkout: Checkout) -> BigDecimal,
                     val isApplicable: (checkout: Checkout) -> Boolean) {
    NOT_AVAILABLE("No promotion",
            fun(checkout: Checkout): BigDecimal {
                var total = BigDecimal.ZERO
                checkout.items.stream().forEach {
                    System.out.println(it.product)
                    total = total.add(it.product.price)
                }
                return total
            },
            fun(checkout: Checkout): Boolean = true),
    UNILEVER("Gets a “3 for 2” deal on Classic Ads",
            fun(checkout: Checkout): BigDecimal {
                var total = BigDecimal.ZERO
                checkout.items.stream().filter {
                    "Classic" != it.product.id
                }.forEach {
                    total = total.add(it.product.price)
                }

                val classicAdsCount = checkout.items.stream().filter {
                    "Classic" == it.product.id
                }.count()
                val divideBy3 = classicAdsCount / 3
                val remainder = classicAdsCount % 3
                val totalClassicAdsPrice = BigDecimal(269.99).multiply(BigDecimal(divideBy3 * 2 + remainder))
                total = total.add(totalClassicAdsPrice)
                return total
            },
            fun(checkout: Checkout): Boolean {
                return true
            }),
    NIKE("Gets a discount on Premium Ads where 4 or more are purchased. The price drops to $379.99 per ad",
            fun(checkout: Checkout): BigDecimal {
                var total = BigDecimal.ZERO
                for (item in checkout.items) {
                    total = if ("Premium" == item.product.id) {
                        total.add(BigDecimal(379.99))
                    } else {
                        total.add(item.product.price)
                    }
                }
                return total
            },
            fun(checkout: Checkout): Boolean {
                var premiumAdsCount = 0
                for (item in checkout.items) {
                    if ("Premium" == item.product.id) {
                        premiumAdsCount++
                    }
                }
                return premiumAdsCount >= 4
            }),
    FORD("Gets a “5 for 4” deal on Classic Ads; " +
            "Gets a discount on Standout Ads where the price drops to $309.99 per ad; " +
            "Gets a discount on Premium Ads when 3 or more are purchased. The price drops to $389.99 per ad",
            fun(checkout: Checkout): BigDecimal {
                var total = BigDecimal.ZERO





                return total
            },
            fun(checkout: Checkout): Boolean {
                return true
            })

}