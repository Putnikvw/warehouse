package com.warehouse.warehouse.service

import com.warehouse.warehouse.data.ProductDataGenerator.createProductDao
import com.warehouse.warehouse.data.ProductDataGenerator.createProductDto
import com.warehouse.warehouse.data.ProductDataGenerator.createProductModel
import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.persistance.repository.ProductRepository
import com.warehouse.warehouse.service.convertor.ProductConvertor
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFalse

@ExtendWith(MockKExtension::class)
class ProductServiceTest {
    @MockK
    private lateinit var repo: ProductRepository

    @MockK
    private lateinit var convertor: ProductConvertor

    @MockK
    private lateinit var response: HttpServletResponse

    @InjectMockKs
    private lateinit var productService: ProductService

    @BeforeEach
    fun setup() = MockKAnnotations.init(this)

    @Test
    fun `saveAll should save only new products that are not already in database`() {
        // Given
        val existingProduct = createProductDao(BigInteger.ONE)
        val newProduct = createProductDao(BigInteger.TWO)
        val productsToSave = listOf(existingProduct, newProduct)

        every { repo.findAll() } returns listOf(existingProduct)
        every { repo.saveAll(listOf(newProduct)) } just Runs

        // When
        productService.saveAll(productsToSave)

        // Then
        verify { repo.findAll() }
        verify { repo.saveAll(listOf(newProduct)) }
    }

    @Test
    fun `saveAll should not save anything when all products already exist`() {
        // Given
        val existingProducts = listOf(createProductDao(ONE))

        every { repo.findAll() } returns emptyList()
        every { repo.saveAll(existingProducts) } just Runs
        // When
        productService.saveAll(existingProducts)

        // Then
        verify { repo.findAll() }
        verify { repo.saveAll(existingProducts) }
    }

    @Test
    fun `saveModel should set error header when product with same title exists`() {
        val productModel = createProductModel()
        // Given
        every { repo.existProductByName("Test Product") } returns true
        every { response.setHeader(any(), any()) } just Runs
        // When
        productService.saveModel(productModel, response)

        // Then
        verify {
            response.setHeader(
                "HX-Trigger",
                """{"showError": {"message": "Product with title 'Test Product' already exists!"}}"""
            )
        }
        verify(exactly = 0) { repo.save(any()) }
    }

    @Test
    fun `saveModel should save product when title does not exist`() {
        val productModel = createProductModel()
        val productDao = createProductDao(ONE)

        // Given
        every { repo.existProductByName("Test Product") } returns false
        every { repo.getLastProductId() } returns Optional.of(BigInteger.valueOf(10))
        every { repo.getLastProductItemId() } returns Optional.of(BigInteger.valueOf(5))
        every { convertor.toCreateProductDao(any(), any()) } returns productDao
        every { convertor.toProductItemDao(any(), any()) } returns mutableListOf()
        every { repo.save(any()) } returns ONE

        // When
        productService.saveModel(productModel, response)

        // Then
        verify { repo.existProductByName("Test Product") }
        verify { repo.getLastProductId() }
        verify { convertor.toCreateProductDao(productModel, BigInteger.valueOf(11)) }
        verify { repo.save(productDao) }
    }

    @Test
    fun `saveModel should not save when lastId is not present`() {
        // Given
        every { repo.existProductByName("Test Product") } returns false
        every { repo.getLastProductId() } returns Optional.empty()

        // When
        productService.saveModel(createProductModel(), response)

        // Then
        verify { repo.getLastProductId() }
        verify(exactly = 0) { repo.save(any()) }
    }

    @Test
    fun `getAll should return all products from repository`() {
        // Given
        val products = listOf(createProductDao(ONE))
        every { repo.findAll() } returns products

        // When
        val result = productService.getAll()

        // Then
        assertEquals(products, result)
        verify { repo.findAll() }
    }

    @Test
    fun `existProductByName should return true when product exists`() {
        // Given
        every { repo.existProductByName("Test Product") } returns true

        // When
        val result = productService.existProductByName("Test Product")

        // Then
        assertTrue(result)
        verify { repo.existProductByName("Test Product") }
    }

    @Test
    fun `existProductByName should return false when product does not exist`() {
        // Given
        every { repo.existProductByName("Non-existent Product") } returns false

        // When
        val result = productService.existProductByName("Non-existent Product")

        // Then
        assertFalse(result)
        verify { repo.existProductByName("Non-existent Product") }
    }

    @Test
    fun `getAllPaginated should return first page correctly`() {
        // Given
        val products = listOf(createProductDao(ONE))
        val productDtos = listOf(createProductDto(ONE))

        every { repo.findAll() } returns products
        every { convertor.run { products.toDtos() } } returns productDtos

        // When
        val result = productService.getAllPaginated(0, 10)

        // Then
        assertEquals(1, result.content.size)
        assertEquals(0, result.currentPage)
        assertEquals(1, result.totalPages)
        assertEquals(1, result.totalElements)
        assertEquals(10, result.pageSize)
        assertFalse(result.hasNext)
        assertFalse(result.hasPrevious)
        assertEquals(0, result.startIndex)
        assertEquals(1, result.endIndex)
    }

    @Test
    fun `getAllPaginated should return empty list when no products exist`() {
        // Given
        every { repo.findAll() } returns emptyList()
        every { convertor.run { any<List<ProductDao>>().toDtos() } } returns emptyList()

        // When
        val result = productService.getAllPaginated(0, 10)

        // Then
        assertTrue(result.content.isEmpty())
        assertEquals(0, result.currentPage)
        assertEquals(1, result.totalPages)
        assertEquals(0, result.totalElements)
    }
}