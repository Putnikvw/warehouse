package com.warehouse.warehouse.controller

import com.ninjasquad.springmockk.MockkBean
import com.warehouse.warehouse.controller.model.PageableWrapper
import com.warehouse.warehouse.data.ProductDataGenerator.createProductDto
import com.warehouse.warehouse.service.ProductService
import com.warehouse.warehouse.service.data.ProductDto
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigInteger

@WebMvcTest(WarehouseController::class)
class WarehouseControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var service: ProductService

    @Test
    fun `home should return home view with activePage attribute`() {
        mockMvc.perform(get("/products"))
            .andExpect(status().isOk)
            .andExpect(view().name("home"))
            .andExpect(model().attribute("activePage", "home"))
    }

    @Test
    fun `getAllProductsTable should return product table fragment with pagination`() {
        // Given
        val productDto = createProductDto(BigInteger.ONE)
        val pageableWrapper = PageableWrapper(
            content = listOf(productDto),
            currentPage = 0,
            totalPages = 1,
            totalElements = 1,
            pageSize = 10,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 1
        )

        every { service.getAllPaginated(0, 10) } returns pageableWrapper

        // When & Then
        mockMvc.perform(
            get("/products/table")
                .param("page", "0")
                .param("pageSize", "10")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(model().attribute("columns", listOf("Title", "Handle", "Product Type")))
            .andExpect(model().attribute("page", pageableWrapper))
            .andExpect(header().string("X-Current-Page", "0"))

        verify(exactly = 1) { service.getAllPaginated(0, 10) }
    }

    @Test
    fun `getAllProductsTable should use default pagination values when not provided`() {
        // Given
        val pageableWrapper = PageableWrapper(
            content = emptyList<ProductDto>(),
            currentPage = 0,
            totalPages = 1,
            totalElements = 0,
            pageSize = 10,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 0
        )

        every { service.getAllPaginated(0, 10) } returns pageableWrapper

        // When & Then
        mockMvc.perform(get("/products/table"))
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(model().attributeExists("columns"))
            .andExpect(model().attributeExists("page"))

        verify(exactly = 1) { service.getAllPaginated(0, 10) }
    }

    @Test
    fun `getAllProductsTable should handle different page sizes`() {
        // Given
        val pageableWrapper = PageableWrapper(
            content = emptyList<ProductDto>(),
            currentPage = 0,
            totalPages = 1,
            totalElements = 25,
            pageSize = 25,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 25
        )

        every { service.getAllPaginated(0, 25) } returns pageableWrapper

        // When & Then
        mockMvc.perform(
            get("/products/table")
                .param("page", "0")
                .param("pageSize", "25")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(header().string("X-Current-Page", "0"))

        verify(exactly = 1) { service.getAllPaginated(0, 25) }
    }

    @Test
    fun `saveProduct should save product and return product table`() {
        // Given
        val pageableWrapper = PageableWrapper(
            content = emptyList<ProductDto>(),
            currentPage = 0,
            totalPages = 1,
            totalElements = 0,
            pageSize = 10,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 0
        )

        every { service.saveModel(any(), any()) } just runs
        every { service.getAllPaginated(0, 10) } returns pageableWrapper

        // When & Then
        mockMvc.perform(
            post("/products")
                .param("title", "New Product")
                .param("handle", "new-product")
                .param("productType", "Electronics")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(model().attributeExists("columns"))
            .andExpect(model().attributeExists("page"))

        verify(exactly = 1) { service.saveModel(any(), any()) }
        verify(exactly = 1) { service.getAllPaginated(0, 10) }
    }

    @Test
    fun `saveProduct should handle product with items`() {
        // Given
        val pageableWrapper = PageableWrapper(
            content = emptyList<ProductDto>(),
            currentPage = 0,
            totalPages = 1,
            totalElements = 0,
            pageSize = 10,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 0
        )

        every { service.saveModel(any(), any()) } just runs
        every { service.getAllPaginated(0, 10) } returns pageableWrapper

        // When & Then
        mockMvc.perform(
            post("/products")
                .param("title", "Product with Items")
                .param("handle", "product-items")
                .param("productType", "Type A")
                .param("productItems[0].title", "Item 1")
                .param("productItems[0].price", "99.99")
                .param("productItems[0].taxable", "true")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))

        verify(exactly = 1) { service.saveModel(any(), any()) }
    }

    @Test
    fun `showAddProductForm should return product form with empty product model`() {
        mockMvc.perform(get("/products/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-form"))
            .andExpect(model().attributeExists("product"))
    }

    @Test
    fun `productItemRow should return product item row fragment`() {
        mockMvc.perform(get("/products/product-item/row"))
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-item-row"))
    }

    @Test
    fun `getAllProductsTable should handle pagination for second page`() {
        // Given
        val pageableWrapper = PageableWrapper(
            content = emptyList<ProductDto>(),
            currentPage = 1,
            totalPages = 3,
            totalElements = 30,
            pageSize = 10,
            hasNext = true,
            hasPrevious = true,
            startIndex = 10,
            endIndex = 20
        )

        every { service.getAllPaginated(1, 10) } returns pageableWrapper

        // When & Then
        mockMvc.perform(
            get("/products/table")
                .param("page", "1")
                .param("pageSize", "10")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(header().string("X-Current-Page", "1"))

        verify(exactly = 1) { service.getAllPaginated(1, 10) }
    }
}