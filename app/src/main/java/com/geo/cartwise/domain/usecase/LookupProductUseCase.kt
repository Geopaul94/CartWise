package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.model.ScannedProduct
import com.geo.cartwise.domain.repository.ProductLookupRepository

class LookupProductUseCase(
    private val repository: ProductLookupRepository
) {
    suspend operator fun invoke(barcode: String): ScannedProduct? = repository.lookupByBarcode(barcode)
}
