package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryRepository

class DeleteGroceryItemUseCase(
    private val repository: GroceryRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteItem(id)
    }
}
