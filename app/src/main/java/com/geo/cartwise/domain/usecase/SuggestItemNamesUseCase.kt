package com.geo.cartwise.domain.usecase

import com.geo.cartwise.domain.repository.GroceryItemRepository

/**
 * Returns up to 5 item names from purchase history that start with [query].
 * Returns an empty list when the query is blank — no point querying for "".
 */
class SuggestItemNamesUseCase(
    private val repository: GroceryItemRepository
) {
    suspend operator fun invoke(query: String): List<String> {
        if (query.isBlank()) return emptyList()
        return repository.suggestItemNames(query.trim())
    }
}
