package com.warehouse.warehouse.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WarehouseController {

    @GetMapping
    fun home(model: Model): String {
        model.addAttribute("activePage", "home")
        return "pages/home"
    }

    @GetMapping("/store")
    fun store(model: Model): String {
        model.addAttribute("activePage", "store")
        return "pages/store"
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): String {
        return "error/404"
    }
}