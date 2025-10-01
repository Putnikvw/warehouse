package com.warehouse.warehouse.controller.model

data class PageableWrapper<T>(
    val content: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Int,
    val pageSize: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val startIndex: Int,
    val endIndex: Int

)
