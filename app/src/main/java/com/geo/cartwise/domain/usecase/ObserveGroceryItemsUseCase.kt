package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.model.GroceryItem
import com.geo.cartwise.domain.repository.GroceryRepository
import kotlinx.coroutines.flow.Flow

/**
 * A "use case" is a class with one job, named after what it does. The ViewModel
 * calls `observeGroceryItems()` instead of reaching into the repository directly —
 * that keeps business rules (like sort order below) out of both the UI and the DB layer.
 */
class ObserveGroceryItemsUseCase(
    private val repository: GroceryRepository
) {
    operator fun invoke(): Flow<List<GroceryItem>> = repository.observeItems()
}
