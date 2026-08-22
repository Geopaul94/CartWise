package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryItemRepository

class DeleteGroceryItemUseCase(
    private val repository: GroceryItemRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteItem(id)
    }
}
