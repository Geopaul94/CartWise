package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.model.GroceryItem
import com.geo.cartwise.domain.repository.GroceryItemRepository
import kotlinx.coroutines.flow.Flow

/**
 * A "use case" is a class with one job, named after what it does. The ViewModel
 * calls `observeGroceryItems(listId)` instead of reaching into the repository
 * directly — that keeps business rules (like sort order in the DAO) out of the UI.
 */
class ObserveGroceryItemsUseCase(
    private val repository: GroceryItemRepository
) {
    operator fun invoke(listId: Long): Flow<List<GroceryItem>> = repository.observeItems(listId)
}
