package com.warehouse.warehouse.persistance.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "product")
data class ProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "title")
    val title: String?,

    @Column(name = "handle")
    val handle: String?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,

    @Column(name = "published_at")
    val publishedAt: LocalDateTime,

    @Column(name = "product_type_id")
    val productTypeId: Long,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "product_type_id", insertable = false, updatable = false)
    val productType: ProductTypeEntity?,

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = [CascadeType.ALL])
    val productItems: MutableList<ProductItemEntity> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductEntity

        if (id != other.id) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }

    override fun toString(): String {
        return "ProductEntity(id=$id, title='$title', createdAt=$createdAt, publishedAt=$publishedAt, updatedAt=$updatedAt, productType=$productType)"
    }
}