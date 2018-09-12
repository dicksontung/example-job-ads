package com.dickson.job.ads

import javax.persistence.*

@Entity
data class User(@Id @GeneratedValue val id: Long? = null,
                val name: String,
                @Enumerated(EnumType.STRING) val promotion: Promotion=Promotion.NOT_AVAILABLE)