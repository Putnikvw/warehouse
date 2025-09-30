package com.warehouse.warehouse.persistance.domain

import java.time.LocalDateTime

data class ProductDao(
    val id: Long,
    val title: String?,
    val handle: String?,
    val productType: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val productItems: MutableList<ProductItemDao> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductDao

        if (id != other.id) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ProductDao(id=$id, title=$title, handle=$handle, productType=$productType, createdAt=$createdAt, updatedAt=$updatedAt, productItems=$productItems)"
    }


}