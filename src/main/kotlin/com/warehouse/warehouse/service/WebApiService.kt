package com.warehouse.warehouse.service

import com.warehouse.warehouse.service.data.Product
import com.warehouse.warehouse.service.data.ProductResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class WebApiService(restClientBuilder: RestClient.Builder) {

    companion object {
        const val BASE_URL = "https://famme.no"
        const val PRODUCT_PATH = "/products.json"
    }

    private val restClient = restClientBuilder.baseUrl(BASE_URL).build();

    fun fetchData(): List<Product> {
        val response = restClient.get()
            .uri(PRODUCT_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(ProductResponse::class.java)

        return response?.products ?: emptyList()
    }

}