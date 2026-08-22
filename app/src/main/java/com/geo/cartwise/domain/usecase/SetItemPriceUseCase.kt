package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryItemRepository

class SetItemPriceUseCase(private val repository: GroceryItemRepository) {
    suspend operator fun invoke(itemId: Long, price: Double) {
        repository.setItemPrice(itemId, price.coerceAtLeast(0.0))
    }
}
