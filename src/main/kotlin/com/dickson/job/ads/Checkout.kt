package com.dickson.job.ads

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Checkout(@Id @GeneratedValue val id: Long? = null,
                    val userId: Long,
                    @OneToMany val items: MutableList<Item> = mutableListOf(),
                    var total: BigDecimal = BigDecimal.ZERO) {
    fun add(item: Item): Checkout {
        items.add(item)
        return this
    }

    fun remove(id: Long): Checkout {
        items.filter {
            it.id != id
        }
        return this
    }

}