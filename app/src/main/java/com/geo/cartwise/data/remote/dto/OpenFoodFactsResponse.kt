package com.geo.cartwise.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shape of https://world.openfoodfacts.org/api/v2/product/{barcode}.json —
 * only the fields we actually use are declared; kotlinx.serialization ignores
 * the rest of the (much larger) real response by default.
 */
@Serializable
data class OpenFoodFactsResponse(
    val status: Int,
    val product: OpenFoodFactsProductDto? = null
)

@Serializable
data class OpenFoodFactsProductDto(
    @SerialName("product_name") val productName: String? = null,
    val brands: String? = null
)
