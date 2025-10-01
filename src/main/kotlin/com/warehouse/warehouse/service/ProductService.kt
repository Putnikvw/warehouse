package com.warehouse.warehouse.service

import com.warehouse.warehouse.controller.model.PageableWrapper
import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.repository.ProductRepository
import com.warehouse.warehouse.service.convertor.ProductConvertor
import com.warehouse.warehouse.service.data.ProductDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(private val repo: ProductRepository, private val convertor: ProductConvertor) {

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

    fun getAllPaginated(page: Int, pageSize: Int): PageableWrapper<ProductDto> {
        val allProducts = convertor.toDtos(getAll())
        val totalElements = allProducts.size
        val totalPages = if (totalElements == 0) 1 else (totalElements + pageSize - 1) / pageSize

        // Ensure page is within valid range
        val validPage = page.coerceIn(0, totalPages - 1)

        val startIndex = validPage * pageSize
        val endIndex = (startIndex + pageSize).coerceAtMost(totalElements)

        val content = if (startIndex < totalElements) {
            allProducts.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
        return PageableWrapper(
            content = content,
            currentPage = validPage,
            totalPages = totalPages,
            totalElements = totalElements,
            pageSize = pageSize,
            hasNext = validPage < totalPages - 1,
            hasPrevious = validPage > 0,
            startIndex = startIndex,
            endIndex = endIndex
        )
    }
}