package com.geo.cartwise.domain.repository

import com.geo.cartwise.domain.model.ScannedProduct

interface ProductLookupRepository {
    /** Returns null if the barcode isn't recognized or the lookup fails (offline, timeout, etc). */
    suspend fun lookupByBarcode(barcode: String): ScannedProduct?
}
