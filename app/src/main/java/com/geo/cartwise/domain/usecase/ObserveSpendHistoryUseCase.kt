package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.model.SpendRecord
import com.geo.cartwise.domain.repository.GroceryItemRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns a live stream of spend records (checked items with a price, grouped
 * by month + aisle). The UI filters these down to whichever month the user
 * has selected from the MonthPicker.
 */
class ObserveSpendHistoryUseCase(
    private val groceryItemRepository: GroceryItemRepository
) {
    operator fun invoke(): Flow<List<SpendRecord>> =
        groceryItemRepository.observeSpendHistory()
}
