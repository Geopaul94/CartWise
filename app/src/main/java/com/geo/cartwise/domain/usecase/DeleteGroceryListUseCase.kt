package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryListRepository

class DeleteGroceryListUseCase(
    private val repository: GroceryListRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteList(id)
    }
}
