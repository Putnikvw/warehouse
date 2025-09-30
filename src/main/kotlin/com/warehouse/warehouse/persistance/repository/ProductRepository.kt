package com.warehouse.warehouse.persistance.repository

import com.warehouse.warehouse.persistance.entity.ProductEntity
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(val jdbcClient: JdbcClient) {

    fun saveAll(entities: List<ProductEntity>) {
        val insert = "INSERT INTO product VALUES(?)"
        for (e in entities) {
            jdbcClient.sql(insert)
                .param(e)
        }
    }
}