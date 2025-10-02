package com.warehouse.warehouse.controller

import com.warehouse.warehouse.controller.model.ProductModel
import com.warehouse.warehouse.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/products")
class WarehouseController(private val service: ProductService) {
    @GetMapping
    fun home(model: Model): String {
        model.addAttribute("activePage", "home")
        return "home"
    }

    @GetMapping("/table")
    fun getAllProductsTable(
        model: Model,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("pageSize", defaultValue = "10") pageSize: Int
    ): String {
        val products = service.getAllPaginated(page, pageSize)
        val columns = listOf("Title", "Handle", "Product Type")
        model.addAttribute("columns", columns)
        model.addAttribute("page", products)
        return "fragments/product-table"
    }

    @PostMapping
    fun saveProduct(@ModelAttribute product: ProductModel, model: Model): String {
        service.saveModel(product)
        return getAllProductsTable(model, 0, 10)
    }

    @GetMapping("/add")
    fun showAddProductForm(model: Model): String {
        model.addAttribute("product", ProductModel(productItems = mutableListOf(ProductModel.ProductItemModel())))
        return "fragments/product-form"
    }

    @GetMapping("/product-item/row")
    fun productItemRow(): String {
        return "fragments/product-item-row"
    }


    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): String {
        return "error/404"
    }
}