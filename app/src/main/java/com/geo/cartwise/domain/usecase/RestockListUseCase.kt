package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryItemRepository

/**
 * Unchecks every item in the list so it's ready for the next shopping trip.
 * "Restock" in the spec means "rebuild last week's list" — in practice that's
 * resetting the check state so all items are live again rather than copying them.
 */
class RestockListUseCase(private val repository: GroceryItemRepository) {
    suspend operator fun invoke(listId: Long) {
        repository.uncheckAll(listId)
    }
}
