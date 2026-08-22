package com.geo.cartwise.presentation.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.geo.cartwise.presentation.common.components.EmptyState
import com.geo.cartwise.presentation.list.components.AddItemBar
import com.geo.cartwise.presentation.list.components.GroceryItemRow

/**
 * Screen-level composable: pure layout + state wiring. All business logic
 * lives in [GroceryListViewModel]; all reusable UI pieces live under
 * presentation/list/components. Keep it that way as this screen grows.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(
    listName: String,
    viewModel: GroceryListViewModel,
    onBack: () -> Unit,
    onScanClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(listName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        bottomBar = {
            AddItemBar(
                value = uiState.inputText,
                onValueChange = viewModel::onInputChange,
                onSubmit = viewModel::onAddItem,
                onScanClick = onScanClick
            )
        }
    ) { paddingValues ->
        if (uiState.items.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.ShoppingCart,
                title = "Your list is empty",
                subtitle = "Add your first item below",
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.items, key = { it.id }) { item ->
                    GroceryItemRow(
                        item = item,
                        onCheckedChange = { checked -> viewModel.onToggleChecked(item.id, checked) },
                        onDelete = { viewModel.onDeleteItem(item.id) }
                    )
                }
            }
        }
    }
}
