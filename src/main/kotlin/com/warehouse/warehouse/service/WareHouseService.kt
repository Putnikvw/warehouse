package com.warehouse.warehouse.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.warehouse.warehouse.persistance.entity.ProductEntity
import com.warehouse.warehouse.persistance.entity.ProductItemEntity
import com.warehouse.warehouse.service.data.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional
class WareHouseService(private val productTypeService: ProductTypeService) {

    fun saveInitialDataFromApi(data: List<Product>) {
        val mapper = ObjectMapper()
        val productTypes = data.stream().map { v -> v.productType }
            .filter { p -> !p?.isEmpty()!! }
            .collect(Collectors.toSet<String>())
        productTypeService.saveAll(productTypes)
        val pTypes = productTypeService.getAll().associate { it.name to it.id }
        val products = data.stream()
            .filter { v -> !v.productType.isEmpty() }
            .map { d ->
            ProductEntity(
                d.id,
                d.title,
                d.handle,
                d.createdAt,
                d.publishedAt,
                pTypes[d.productType]!!,
                d.updatedAt,
                null,
                d.items.stream().map { i ->
                    ProductItemEntity(
                        i.id,
                        i.title,
                        i.price,
                        i.taxable,
                        mapper.writeValueAsString(i.featureImg),
                        d.id,
                        null
                    )
                }.toList()
            )
        }.toList()
        print(products)
    }
}