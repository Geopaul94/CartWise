package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryListRepository

class CreateGroceryListUseCase(
    private val repository: GroceryListRepository
) {
    suspend operator fun invoke(name: String): Long? {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return null
        return repository.createList(trimmed)
    }
}
