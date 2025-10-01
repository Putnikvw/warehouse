package com.warehouse.warehouse.controller

import com.warehouse.warehouse.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class WarehouseController(private val service: ProductService) {
    @GetMapping
    fun home(model: Model): String {
        model.addAttribute("activePage", "home")
        return "home"
    }

    @GetMapping("/products")
    fun products(
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

    @GetMapping("/products/form")
    fun showAddProductForm(): String {
        return "fragments/product-form"
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): String {
        return "error/404"
    }
}