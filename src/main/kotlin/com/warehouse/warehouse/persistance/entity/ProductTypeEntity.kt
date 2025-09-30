package com.warehouse.warehouse.persistance.entity

import jakarta.persistence.*

@Entity
@Table(name = "product_type")
data class ProductTypeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "name")
    val name: String,

//    @OneToMany(mappedBy = "productType", orphanRemoval = true, cascade = [CascadeType.ALL])
//    val products: MutableList<ProductEntity> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductTypeEntity

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "ProductTypeEntity(id=$id, name='$name')"
    }
}