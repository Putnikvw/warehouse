package com.warehouse.warehouse.service

import com.warehouse.warehouse.controller.model.PageableWrapper
import com.warehouse.warehouse.controller.model.ProductModel
import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.repository.ProductRepository
import com.warehouse.warehouse.service.convertor.ProductConvertor
import com.warehouse.warehouse.service.data.ProductDto
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger

@Service
@Transactional
class ProductService(private val repo: ProductRepository, private val convertor: ProductConvertor) {

    fun saveAll(products: List<ProductDao>) {
        val savedProductIds = repo.findAll().map { it.id }.toList()
        val toSave = products.filter { !savedProductIds.contains(it.id) }.toList()
        repo.saveAll(toSave.toList())
    }

    fun saveModel(product: ProductModel, response: HttpServletResponse) {
        if (existProductByName(product.title!!)) {
            response.setHeader(
                "HX-Trigger",
                """{"showError": {"message": "Product with title '${product.title}' already exists!"}}"""
            )
            return
        }
        val lastId = repo.getLastProductId()
        if (lastId.isPresent) {
            val newId = lastId.get().add(BigInteger.ONE)
            val productDao = convertor.toCreateProductDao(product, newId)
            saveProductItems(product, productDao)
            save(productDao)
        }
    }

    private fun saveProductItems(
        product: ProductModel,
        productDao: ProductDao
    ) {
        val lastProductItemId = repo.getLastProductItemId()
        if (lastProductItemId.isPresent) {
            val newProductItemId = lastProductItemId.get()
            val items = convertor.toProductItemDao(product.productItems, newProductItemId)
            productDao.productItems.addAll(items)
        }
    }

    fun save(productDao: ProductDao) {
        repo.save(productDao)
    }

    fun getAll(): List<ProductDao> {
        return repo.findAll()
    }

    fun existProductByName(title: String): Boolean {
        return repo.existProductByName(title)
    }

    fun getAllPaginated(page: Int, pageSize: Int): PageableWrapper<ProductDto> {
        val products = getAll()
        val allProducts = convertor.run { products.toDtos() }
        return paginate(allProducts, page, pageSize)
    }

    // created by AI
    fun getAllPaginatedSearch(page: Int, pageSize: Int, q: String): PageableWrapper<ProductDto> {
        val products = getAll()
        val allProducts = convertor.run { products.toDtos() }
        val filtered = if (q.isBlank()) allProducts else allProducts.filter { it.title?.contains(q, ignoreCase = true) == true }
        return paginate(filtered, page, pageSize)
    }

    private fun paginate(allProducts: List<ProductDto>, page: Int, pageSize: Int): PageableWrapper<ProductDto> {
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