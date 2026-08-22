package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.model.GroceryList
import com.geo.cartwise.domain.repository.GroceryListRepository
import kotlinx.coroutines.flow.Flow

class ObserveGroceryListsUseCase(
    private val repository: GroceryListRepository
) {
    operator fun invoke(): Flow<List<GroceryList>> = repository.observeLists()
}
