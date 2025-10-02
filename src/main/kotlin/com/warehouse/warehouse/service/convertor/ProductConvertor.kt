package com.warehouse.warehouse.service.convertor

import com.warehouse.warehouse.controller.model.ProductModel
import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.domain.ProductItemDao
import com.warehouse.warehouse.service.data.ProductDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

@Component
class ProductConvertor {

    fun toDto(product: ProductDao): ProductDto {
        return ProductDto(
            id = product.id,
            title = product.title,
            handle = product.handle,
            productType = product.productType,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }

    fun toDtos(products: List<ProductDao>): List<ProductDto> {
        return products.map { toDto(it) }
    }

    fun toCreateProductDao(model: ProductModel, newId: BigInteger): ProductDao {
        return ProductDao(
            id = newId,
            title = model.title,
            handle = model.handle,
            productType = model.productType,
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )
    }

    fun toProductItemDao(itemModels: List<ProductModel.ProductItemModel>, newId: BigInteger): MutableList<ProductItemDao> {
        return itemModels.map { i ->
            ProductItemDao(
                id = newId.inc(),
                title = i.title ?: "",
                price = i.price ?: BigDecimal.ZERO,
                taxable = i.taxable,
                featureImg = "",
                productId = BigInteger.ZERO
            )
        }.toMutableList()
    }
}