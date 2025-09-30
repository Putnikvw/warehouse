package com.warehouse.warehouse.persistance.domain

import java.math.BigDecimal

data class ProductItemDao(

    val id: Long,
    val title: String,
    val price: BigDecimal,
    val taxable: Boolean,
    val featureImg: String,
    val productId: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductItemDao

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ProductItemDao(id=$id, title='$title', price=$price, taxable=$taxable, featureImg='$featureImg', productId=$productId)"
    }


}