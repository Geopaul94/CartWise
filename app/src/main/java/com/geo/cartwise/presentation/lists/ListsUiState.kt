package com.geo.cartwise.presentation.lists

import com.geo.cartwise.domain.model.GroceryList

data class ListsUiState(
    val lists: List<GroceryList> = emptyList(),
    val isCreateDialogOpen: Boolean = false,
    val newListName: String = ""
)
