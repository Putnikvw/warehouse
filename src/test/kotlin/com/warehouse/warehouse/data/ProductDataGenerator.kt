package com.warehouse.warehouse.data

import com.warehouse.warehouse.controller.model.ProductModel
import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.service.data.ProductDto
import java.math.BigInteger
import java.time.LocalDateTime

object ProductDataGenerator {

    fun createProductDao(id: BigInteger) = ProductDao(
        id = id,
        title = "Test Product",
        handle = "test-product",
        productType = "Electronics",
        createdAt = LocalDateTime.now().minusDays(2),
        updatedAt = LocalDateTime.now(),
        productItems = mutableListOf(),
    )

    fun createProductModel() = ProductModel(
        id = 1L,
        title = "Test Product",
        handle = "test-product",
        productType = "Electronics",
        productItems = mutableListOf()
    )

    fun createProductDto(id: BigInteger) = ProductDto(
        id = id,
        title = "Test Product",
        handle = "test-product",
        productType = "Electronics",
        createdAt = LocalDateTime.now().minusDays(2),
        updatedAt = LocalDateTime.now()
    )
}