package com.warehouse.warehouse.controller

import com.warehouse.warehouse.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WarehouseController(private val service: ProductService) {

    @GetMapping
    fun home(model: Model): String {
        model.addAttribute("activePage", "home")
        return "pages/home"
    }

    @GetMapping("/products")
    fun products(model: Model): String {
        val products = service.getAll()
        val columns = listOf("Title", "Handle", "Product Type")
        model.addAttribute("columns", columns)
        model.addAttribute("products", products)
        return "layout/product-table"
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): String {
        return "error/404"
    }
}