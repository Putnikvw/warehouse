package com.warehouse.warehouse.service.convertor

import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.service.data.ProductDto
import org.springframework.stereotype.Component

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
}