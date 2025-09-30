package com.warehouse.warehouse.persistance.repository

import com.warehouse.warehouse.persistance.entity.ProductTypeEntity
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductTypeRepository(val jdbcClient: JdbcClient) {

    fun getAll(): List<ProductTypeEntity> {
        val sql = "select * from product_type"
        return jdbcClient.sql(sql)
            .query(ProductTypeEntity::class.java)
            .list()
    }

    fun findProductTypeBaName(name: String): List<ProductTypeEntity> {
        val sql = "select * from product_type as pt where pt.name=:name"
        return jdbcClient.sql(sql)
            .params("name", name)
            .query(ProductTypeEntity::class.java).list()
    }

    fun saveAllByName(values: Set<String>) {
        val insert = "INSERT INTO product_type (name) VALUES (?)".trimIndent()
        values.forEach { n ->
            jdbcClient.sql(insert)
                .param(n)
                .update()
        }
    }
}