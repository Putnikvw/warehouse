package com.warehouse.warehouse.persistance.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "product_item")
data class ProductItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "title")
    val title: String,

    @Column(name = "price")
    val price: BigDecimal,

    @Column(name = "taxable")
    val taxable: Boolean,

    @Column(name = "feature_img")
    val featureImg: String,

    @Column(name = "product_id")
    val productId: Long,

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    val product: ProductEntity?

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductItemEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ProductItemEntity(id=$id, title='$title', price=$price, taxable=$taxable, featureImg='$featureImg')"
    }
}