package com.warehouse.warehouse.controller

import com.warehouse.warehouse.controller.model.PageableWrapper
import com.warehouse.warehouse.controller.model.ProductModel
import com.warehouse.warehouse.data.ProductDataGenerator.createProductDto
import com.warehouse.warehouse.service.ProductService
import com.warehouse.warehouse.service.data.ProductDto
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigInteger

@WebMvcTest(WarehouseController::class)
class WarehouseControllerWebMvcTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockitoBean
    lateinit var service: ProductService

    // 1. home()
    @Test
    fun `home endpoint should return home view`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("home"))
            .andExpect(model().attribute("activePage", "home"))
    }

    // 2. getAllProductsTable()
    @Test
    fun `getAllProductsTable should return table fragment`() {
        val page = 0
        val pageSize = 10
        val product = createProductDto(BigInteger.ONE)
        val pageWrapper = PageableWrapper(
            content = listOf(product),
            currentPage = 0,
            totalPages = 1,
            totalElements = 1,
            pageSize = 10,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 1
        )

        given(service.getAllPaginated(page, pageSize)).willReturn(pageWrapper)

        mockMvc.perform(
            get("/table")
                .param("page", page.toString())
                .param("pageSize", pageSize.toString())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(model().attributeExists("columns"))
            .andExpect(model().attribute("page", pageWrapper))
            .andExpect(header().string("X-Current-Page", "0"))
    }

    // 3. saveProduct()
    @Test
    fun `saveProduct should save and return table fragment`() {
        val productModel = ProductModel(
            id = 1L,
            title = "New Product",
            handle = "new-product",
            productType = "Type A",
            productItems = mutableListOf()
        )

        val pageWrapper = PageableWrapper<ProductDto>(
            content = emptyList(),
            currentPage = 0,
            totalPages = 1,
            totalElements = 0,
            pageSize = 10,
            hasNext = false,
            hasPrevious = false,
            startIndex = 0,
            endIndex = 0
        )

        given(service.getAllPaginated(0, 10)).willReturn(pageWrapper)

        mockMvc.perform(
            post("/")
                .flashAttr("product", productModel)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-table"))
            .andExpect(model().attributeExists("columns"))
            .andExpect(model().attribute("page", pageWrapper))
    }

    // 4. showAddProductForm()
    @Test
    fun `showAddProductForm should return product form fragment`() {
        mockMvc.perform(get("/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-form"))
            .andExpect(model().attributeExists("product"))
    }

    // 5. productItemRow()
    @Test
    fun `productItemRow should return item row fragment`() {
        mockMvc.perform(get("/product-item/row"))
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/product-item-row"))
    }
}