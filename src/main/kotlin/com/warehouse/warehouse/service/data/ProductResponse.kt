package com.warehouse.warehouse.service.data

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

data class ProductResponse(
    val products: List<Product>
) {
    data class Product(
        val id: BigInteger,
        val title: String?,
        val handle: String?,

        @JsonProperty("published_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        val publishedAt: LocalDateTime,

        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        val createdAt: LocalDateTime,

        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        val updatedAt: LocalDateTime,
        @JsonProperty("product_type")
        val productType: String,
        @JsonProperty("variants")
        val items: List<ProductItem>
    ) {
        data class ProductItem(
            val id: BigInteger,
            val title: String,
            val price: BigDecimal,
            val taxable: Boolean,
            @JsonProperty("featured_image")
            val featureImg: ProductItemDetails?
        ) {
            data class ProductItemDetails(
                val id: Long,
                @JsonProperty("product_id")
                val productId: Long,
                val src: String?
            )
        }
    }
}