package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryListRepository

class SetListBudgetUseCase(private val repository: GroceryListRepository) {
    suspend operator fun invoke(listId: Long, budget: Double) {
        repository.setListBudget(listId, budget.coerceAtLeast(0.0))
    }
}
