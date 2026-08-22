package com.geo.cartwise.presentation.list

import com.geo.cartwise.domain.model.GroceryItem

/**
 * One group of unchecked items sharing the same store aisle.
 * Checked items are tracked separately in [GroceryListUiState.checkedItems]
 * so they can be rendered as a single "Checked" section at the bottom.
 */
data class AisleGroup(
    val aisle: String,
    val items: List<GroceryItem>
)

data class GroceryListUiState(
    val aisleGroups: List<AisleGroup> = emptyList(),
    val checkedItems: List<GroceryItem> = emptyList(),
    val inputText: String = ""
)
