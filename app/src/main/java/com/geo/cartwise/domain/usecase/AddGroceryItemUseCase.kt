package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryRepository

class AddGroceryItemUseCase(
    private val repository: GroceryRepository
) {
    suspend operator fun invoke(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        repository.addItem(trimmed)
    }
}
