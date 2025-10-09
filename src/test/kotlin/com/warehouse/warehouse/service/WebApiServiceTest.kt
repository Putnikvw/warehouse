package com.warehouse.warehouse.service

import com.warehouse.warehouse.service.data.ProductResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import java.math.BigInteger
import java.time.LocalDateTime

class WebApiServiceTest {

    @MockK
    lateinit var restClientBuilder: RestClient.Builder
    @MockK
    lateinit var restClient: RestClient
    @MockK
    lateinit var requestHeadersUriSpec: RestClient.RequestHeadersUriSpec<*>
    @MockK
    lateinit var responseSpec: RestClient.ResponseSpec
    @MockK
    lateinit var webApiService: WebApiService


    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        every { restClientBuilder.baseUrl(WebApiService.BASE_URL) } returns restClientBuilder
        every { restClientBuilder.build() } returns restClient

        webApiService = WebApiService(restClientBuilder)
    }


    @Test
    fun `fetchData should return list of products when response is successful`() {
        // Given
        val product1 = ProductResponse.Product(
            id = BigInteger.ONE,
            title = "Product 1",
            handle = "product-1",
            publishedAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now().minusDays(2),
            updatedAt = LocalDateTime.now().plusDays(1),
            productType = "Type A",
            items = listOf()

        )
        val product2 = ProductResponse.Product(
            id = BigInteger.TWO,
            title = "Product 2",
            handle = "product-2",
            publishedAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now().minusDays(2),
            updatedAt = LocalDateTime.now().plusDays(1),
            productType = "Type B",
            items = listOf()
        )
        val expectedResponse = ProductResponse(products = listOf(product1, product2))

        every { restClient.get() } returns requestHeadersUriSpec
        every { requestHeadersUriSpec.uri(WebApiService.PRODUCT_PATH) } returns requestHeadersUriSpec
        every { requestHeadersUriSpec.accept(MediaType.APPLICATION_JSON) } returns requestHeadersUriSpec
        every { requestHeadersUriSpec.retrieve() } returns responseSpec
        every { responseSpec.body(ProductResponse::class.java) } returns expectedResponse

        // When
        val result = webApiService.fetchData()

        // Then
        assertEquals(2, result.size)
        assertEquals("Product 1", result[0].title)
        assertEquals("Product 2", result[1].title)

        verify(exactly = 1) { restClient.get() }
        verify(exactly = 1) { requestHeadersUriSpec.uri(WebApiService.PRODUCT_PATH) }
        verify(exactly = 1) { requestHeadersUriSpec.accept(MediaType.APPLICATION_JSON) }
        verify(exactly = 1) { requestHeadersUriSpec.retrieve() }
        verify(exactly = 1) { responseSpec.body(ProductResponse::class.java) }
    }
}