package com.warehouse.warehouse.service

import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(private val repo: ProductRepository) {

    fun saveAll(products: List<ProductDao>) {
        val savedProductIds = repo.findAll().map { it.id }.toList()
        val toSave = products.filter { !savedProductIds.contains(it.id) }.toList()
        repo.saveAll(toSave.toList())
    }

    fun save(productDao: ProductDao) {
        repo.save(productDao)
    }

    fun getAll(): List<ProductDao> {
        return repo.findAll()
    }
}