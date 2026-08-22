package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryItemRepository

class AddGroceryItemUseCase(
    private val repository: GroceryItemRepository
) {
    suspend operator fun invoke(listId: Long, name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        repository.addItem(listId, trimmed)
    }
}
