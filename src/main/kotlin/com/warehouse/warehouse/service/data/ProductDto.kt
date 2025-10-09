package com.warehouse.warehouse.service.data

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

data class ProductDto(
    val id: BigInteger,
    val title: String?,
    val handle: String?,
    val productType: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val itemTitle: String,
    val itemPrice: BigDecimal,
    val taxable: Boolean,
    val featureImg: String?,
)
