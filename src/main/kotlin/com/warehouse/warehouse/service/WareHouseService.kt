package com.warehouse.warehouse.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.domain.ProductItemDao
import com.warehouse.warehouse.service.data.ProductResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WareHouseService(private val productService: ProductService) {

    companion object {
        val mapper = ObjectMapper()
    }

    fun saveInitialDataFromApi(data: List<ProductResponse.Product>) {
        val products = convertDataToProductDaoFromApi(data)
        productService.saveAll(products)
    }

    private fun convertDataToProductDaoFromApi(data: List<ProductResponse.Product>): List<ProductDao> {
        return data.stream()
            .map { d ->
                ProductDao(
                    d.id,
                    d.title,
                    d.handle,
                    d.productType,
                    d.createdAt,
                    d.updatedAt,
                    d.items.stream().map { i ->
                        ProductItemDao(
                            i.id,
                            i.title,
                            i.price,
                            i.taxable,
                            mapper.writeValueAsString(i.featureImg),
                            d.id,
                        )
                    }.toList()
                )
            }.toList()
    }
}