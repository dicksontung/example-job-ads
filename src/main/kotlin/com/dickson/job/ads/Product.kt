package com.dickson.job.ads

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Product(@Id val id: String,
                   val name: String? = null,
                   val price: BigDecimal? = null)