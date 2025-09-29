package com.warehouse.warehouse.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class WebApiService(private val restClientBuilder: RestClient.Builder) {

    private val restClient = restClientBuilder.baseUrl("https://fame.no").build();

    fun fetchData(): String? {
        return restClient.get()
            .uri("/products.json")
            .retrieve()
            .body(String::class.java);
    }

}