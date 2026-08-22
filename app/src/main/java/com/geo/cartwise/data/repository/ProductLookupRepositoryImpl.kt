package com.geo.cartwise.data.repository

import com.geo.cartwise.data.remote.OpenFoodFactsApi
import com.geo.cartwise.domain.model.ScannedProduct
import com.geo.cartwise.domain.repository.ProductLookupRepository
import retrofit2.HttpException
import java.io.IOException

class ProductLookupRepositoryImpl(
    private val api: OpenFoodFactsApi
) : ProductLookupRepository {

    override suspend fun lookupByBarcode(barcode: String): ScannedProduct? {
        return try {
            val response = api.getProduct(barcode)
            val name = response.product?.productName?.trim()
            if (response.status != 1 || name.isNullOrEmpty()) {
                null
            } else {
                ScannedProduct(barcode = barcode, name = name)
            }
        } catch (e: IOException) {
            // Network unreachable / timeout — treat like "not found" so the
            // scanner screen can show one consistent error message.
            null
        } catch (e: HttpException) {
            // e.g. barcode not in the Open Food Facts database (404).
            null
        }
    }
}
