package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryItemRepository

class SetItemCheckedUseCase(
    private val repository: GroceryItemRepository
) {
    suspend operator fun invoke(id: Long, isChecked: Boolean) {
        repository.setChecked(id, isChecked)
    }
}
