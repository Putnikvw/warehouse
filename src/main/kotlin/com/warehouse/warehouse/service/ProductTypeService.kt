package com.warehouse.warehouse.service

import com.warehouse.warehouse.persistance.entity.ProductTypeEntity
import com.warehouse.warehouse.persistance.repository.ProductTypeRepository
import org.springframework.stereotype.Service

@Service
class ProductTypeService(private val repo: ProductTypeRepository) {

    fun save() {

    }

    fun saveAll(productTypes: Set<String>) {
        var listToSave = productTypes;
        val existProductTypes = repo.getAll().map { it.name }
        if (!existProductTypes.isEmpty()) {
            listToSave = productTypes
                .filter { n -> !existProductTypes.contains(n) }
                .toSet()
        }
        repo.saveAllByName(listToSave)
    }

    fun getAll(): List<ProductTypeEntity> {
        return repo.getAll()
    }
}