package com.dickson.job.ads

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class Item(@Id @GeneratedValue val id: Long? = null,
                val name: String,
                @ManyToOne(optional = false)
                @JoinColumn(name = "productId", nullable = false)
                @OnDelete(action = OnDeleteAction.CASCADE) val product: Product)