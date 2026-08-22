package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryRepository

class SetItemCheckedUseCase(
    private val repository: GroceryRepository
) {
    suspend operator fun invoke(id: Long, isChecked: Boolean) {
        repository.setChecked(id, isChecked)
    }
}
