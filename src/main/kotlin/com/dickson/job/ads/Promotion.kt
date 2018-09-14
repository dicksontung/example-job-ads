package com.dickson.job.ads

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.streams.toList

enum class Promotion(val description: String,
                     val calcPrice: (checkout: Checkout) -> BigDecimal,
                     val isApplicable: (checkout: Checkout) -> Boolean) {
    NOT_AVAILABLE("No promotion",
            fun(checkout: Checkout): BigDecimal {
                var total = BigDecimal.ZERO
                checkout.items.stream().forEach {
                    total = total.add(it.product.price)
                }
                return scale(total)
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
                val classicAds = checkout.items.stream().filter {
                    "Classic" == it.product.id
                }.toList()
                val classicAdsPrice = classicAds.getOrNull(0)?.product?.price
                        ?: return BigDecimal.ZERO
                val totalClassicAdsPrice = xForYDeals(3, 2, classicAds.size, classicAdsPrice)
                total = total.add(totalClassicAdsPrice)
                return scale(total)
            },
            fun(checkout: Checkout): Boolean {
                return true
            }),
    APPLE(
            "Gets a discount on Standout Ads where the price drops to $299.99 per ad",
            fun(checkout: Checkout): BigDecimal {
                var total = BigDecimal.ZERO
                for (item in checkout.items) {
                    total = if ("Standout" == item.product.id) {
                        total.add(BigDecimal(299.99))
                    } else {
                        total.add(item.product.price)
                    }
                }
                return scale(total)
            },
            fun(checkout: Checkout): Boolean {
                return true
            }
    ),
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
                return scale(total)
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
                var map = checkout.items.groupBy {
                    it.product.id
                }
                val classicAds = map.get("Classic")
                val classicAdsPrice = classicAds?.getOrNull(0)?.product?.price ?: BigDecimal.ZERO
                total = total.add(xForYDeals(5, 4, classicAds?.size ?: 0, classicAdsPrice))

                val standoutAdsCount = map.get("Standout")?.size ?: 0
                total = total.add(BigDecimal(309.99).multiply(BigDecimal(standoutAdsCount)))

                val premiumAdsCount = map.get("Premium")?.size ?: 0
                val premiumNormalPrice = map.get("Premium")?.getOrNull(0)?.product?.price
                        ?: BigDecimal.ZERO
                total = if (premiumAdsCount >= 3) {
                    total.add(BigDecimal(389.99).multiply(BigDecimal(premiumAdsCount)))
                } else {
                    total.add(premiumNormalPrice.multiply(BigDecimal(premiumAdsCount)))
                }
                return scale(total)
            },
            fun(checkout: Checkout): Boolean {
                return true
            });

    companion object {
        fun xForYDeals(x: Int, y: Int, itemCount: Int, price: BigDecimal): BigDecimal {
            val itemCountAfterDeal: Int = itemCount / x * y + itemCount % x
            return price.multiply(BigDecimal(itemCountAfterDeal))
        }

        fun scale(price: BigDecimal): BigDecimal = price.setScale(2, RoundingMode.HALF_UP)
    }
}