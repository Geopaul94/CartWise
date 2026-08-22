package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryListRepository
import kotlinx.coroutines.flow.Flow

class ObserveListBudgetUseCase(private val repository: GroceryListRepository) {
    operator fun invoke(listId: Long): Flow<Double> = repository.observeListBudget(listId)
}
