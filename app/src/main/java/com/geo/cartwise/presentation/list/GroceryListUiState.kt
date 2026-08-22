package com.geo.cartwise.presentation.list

import com.geo.cartwise.domain.model.GroceryItem

data class GroceryListUiState(
    val items: List<GroceryItem> = emptyList(),
    val inputText: String = ""
)
