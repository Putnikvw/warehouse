package com.warehouse.warehouse.config

import com.warehouse.warehouse.service.WareHouseService
import com.warehouse.warehouse.service.WebApiService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class Config(private val webService: WebApiService, private val service: WareHouseService) {

    @Scheduled(initialDelay = 0L)
    fun runReadJobApi() {
        val response = webService.fetchData()
        service.saveInitialDataFromApi(response)
    }
}