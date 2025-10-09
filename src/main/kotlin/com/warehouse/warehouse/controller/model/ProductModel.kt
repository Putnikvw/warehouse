package com.warehouse.warehouse.controller.model

import java.math.BigDecimal

data class ProductModel(
    var id: Long? = null,
    var title: String? = null,
    var handle: String? = null,
    var productType: String? = null,
    var productItems: MutableList<ProductItemModel> = mutableListOf()
) {
    data class ProductItemModel(
        var id: Long? = null,
        var title: String? = null,
        var price: BigDecimal? = null,
        var taxable: Boolean = false
    )
}
