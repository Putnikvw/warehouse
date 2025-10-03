package com.warehouse.warehouse.service

import com.warehouse.warehouse.persistance.domain.ProductDao
import com.warehouse.warehouse.service.data.ProductResponse
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class WareHouseServiceTest {

    @MockK
    private lateinit var productService: ProductService

    @InjectMockKs
    private lateinit var wareHouseService: WareHouseService

    @Test
    fun `saveInitialDataFromApi should convert and save products`() {
        // Given
        val featureImg = ProductResponse.Product.ProductItem.ProductItemDetails(
            233L,
            21L,
            "https://test.com"
        )
        val productItem = ProductResponse.Product.ProductItem(
            id = BigInteger.valueOf(100),
            title = "Item 1",
            price = BigDecimal("99.99"),
            taxable = true,
            featureImg = featureImg
        )
        val product = ProductResponse.Product(
            id = BigInteger.ONE,
            title = "Test Product",
            handle = "test-product",
            productType = "Electronics",
            publishedAt = LocalDateTime.now().minusDays(3),
            createdAt = LocalDateTime.now().minusDays(4),
            updatedAt = LocalDateTime.now(),
            items = listOf(productItem)
        )
        val apiData = listOf(product)

        every { productService.saveAll(any()) } just Runs

        // When
        wareHouseService.saveInitialDataFromApi(apiData)

        // Then
        val slot = slot<List<ProductDao>>()
        verify { productService.saveAll(capture(slot)) }

        val savedProducts = slot.captured
        assert(savedProducts.size == 1)
        assert(savedProducts[0].id == BigInteger.ONE)
        assert(savedProducts[0].title == "Test Product")
        assert(savedProducts[0].handle == "test-product")
        assert(savedProducts[0].productType == "Electronics")
        assert(savedProducts[0].productItems.size == 1)
        assert(savedProducts[0].productItems[0].title == "Item 1")
        assert(savedProducts[0].productItems[0].price == BigDecimal("99.99"))
        assert(savedProducts[0].productItems[0].taxable)

    }

    @Test
    fun `saveInitialDataFromApi should handle empty list`() {
        // Given
        val emptyApiData = emptyList<ProductResponse.Product>()

        every { productService.saveAll(emptyList()) } just Runs

        // When
        wareHouseService.saveInitialDataFromApi(emptyApiData)

        // Then
        verify(exactly = 1) { productService.saveAll(emptyList()) }
    }
}