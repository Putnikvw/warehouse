package com.warehouse.warehouse.persistance.repository

import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.domain.ProductItemDao
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.*

@Repository
class ProductRepository(val jdbc: JdbcClient) {

    fun saveAll(products: List<ProductDao>) {
        products.forEach { product ->
            save(product)
        }
    }

    fun save(product: ProductDao): BigInteger {
        val newId = jdbc.sql(
            """
            INSERT INTO product(id, title, handle, product_type)
                    VALUES (:id, :title, :handle, :productType)
                    RETURNING id"""
        )
            .param("id", product.id)
            .param("title", product.title)
            .param("handle", product.handle)
            .param("productType", product.productType)
            .query(BigInteger::class.java)
            .single()

        // Save child items if present
        product.productItems.forEach { saveItem(it.copy(productId = newId)) }

        return newId
    }

    fun findAll(): List<ProductDao> {
        return jdbc.sql(
            """
            SELECT id, title, handle, product_type, created_at, updated_at
            FROM product
            """
        ).query { rs, _ ->
            ProductDao(
                id = rs.getLong("id").toBigInteger(),
                title = rs.getString("title"),
                handle = rs.getString("handle"),
                productType = rs.getString("product_type"),
                createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
                updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime(),
                productItems = mutableListOf() // empty by default
            )
        }
            .list()
    }

    fun findById(id: Long): ProductDao? {
        val product = jdbc.sql("SELECT * FROM product WHERE id = :id")
            .param("id", id)
            .query(ProductDao::class.java)
            .single()

        if (product != null) {
            val items = findItemsByProductId(id)
            return product.copy(productItems = items)
        }
        return null
    }

    fun getLastProductId(): Optional<BigInteger> {
        val sql = "select max(id) from product"
        return jdbc.sql(sql)
            .query(BigInteger::class.java)
            .optional()
    }

    fun existProductByName(title: String): Boolean {
        val sql = "select count(*) from product where title = :title"
        val count = jdbc.sql(sql)
            .param("title", title)
            .query(Int::class.java)
            .single()
        return count > 0
    }

    // ----- ProductItem CRUD -----

    fun getLastProductItemId(): Optional<BigInteger> {
        val sql = "select max(id) from product_item"
        return jdbc.sql(sql)
            .query(BigInteger::class.java)
            .optional()
    }

    fun findItemsByProductId(productId: Long): MutableList<ProductItemDao> =
        jdbc.sql("SELECT * FROM product_item WHERE product_id = :pid")
            .param("pid", productId)
            .query(ProductItemDao::class.java)
            .list()

    fun saveItem(item: ProductItemDao): Long =
        jdbc.sql(
            "INSERT INTO product_item(id, product_id, title, price, taxable) " +
                    "VALUES(:id, :pid, :title, :price, :taxable) RETURNING id"
        )
            .param("id", item.id)
            .param("pid", item.productId)
            .param("title", item.title)
            .param("price", item.price)
            .param("taxable", item.taxable)
            .query(Long::class.java)
            .single()
}