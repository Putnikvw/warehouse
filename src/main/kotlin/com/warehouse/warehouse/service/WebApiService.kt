package com.warehouse.warehouse.service

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class WebApiService(private val restClientBuilder: RestClient.Builder) {

    private val restClient = restClientBuilder.baseUrl("https://famme.no").build();

    fun fetchData(): List<Product> {
        val response = restClient.get()
            .uri("/products.json")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(ProductResponse::class.java)

        return response?.products ?: emptyList()
    }

}