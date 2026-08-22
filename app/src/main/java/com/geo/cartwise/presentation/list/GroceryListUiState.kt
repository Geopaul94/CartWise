package com.geo.cartwise.presentation.list

import com.geo.cartwise.domain.model.GroceryItem

data class AisleGroup(
    val aisle: String,
    val items: List<GroceryItem>
)

data class GroceryListUiState(
    val aisleGroups: List<AisleGroup> = emptyList(),
    val checkedItems: List<GroceryItem> = emptyList(),
    val inputText: String = "",
    val priceInput: String = "",
    val budget: Double = 0.0,
    val totalEstimatedPrice: Double = 0.0,
    val showSetBudgetDialog: Boolean = false
)
