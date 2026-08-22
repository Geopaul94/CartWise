package com.geo.cartwise.data.remote

import com.geo.cartwise.data.remote.dto.OpenFoodFactsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}.json?fields=product_name,brands")
    suspend fun getProduct(@Path("barcode") barcode: String): OpenFoodFactsResponse
}
