package com.warehouse.warehouse.config

import com.warehouse.warehouse.service.WebApiService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class Config(private val webService: WebApiService) {

    @Scheduled(initialDelay = 0L)
    fun runReadJobApi() {
        println(webService.fetchData())
    }
}