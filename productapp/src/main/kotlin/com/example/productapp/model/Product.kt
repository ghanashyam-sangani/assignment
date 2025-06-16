package com.example.productapp.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: Int? = null,
    val title: String,
    val vendor: String?,
    val price: BigDecimal?,
    val variants: String?, // Stored as JSON string
    val createdAt: LocalDateTime? = null
)
