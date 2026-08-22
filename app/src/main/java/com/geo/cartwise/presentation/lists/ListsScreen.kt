package com.geo.cartwise.presentation.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geo.cartwise.presentation.common.components.EmptyState
import com.geo.cartwise.presentation.lists.components.CreateListDialog
import com.geo.cartwise.presentation.lists.components.ListCard

/**
 * Home screen: one card per shopping list (supermarket, chemist, wholesale
 * club, ...). Tapping a card opens that list's items via [onOpenList].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    viewModel: ListsViewModel,
    onOpenList: (listId: Long, listName: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.openListEvents.collect { event -> onOpenList(event.listId, event.listName) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CartWise") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onCreateListClick) {
                Icon(Icons.Filled.Add, contentDescription = "New list")
            }
        }
    ) { paddingValues ->
        if (uiState.lists.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.ShoppingCart,
                title = "No lists yet",
                subtitle = "Tap + to start your first shopping list",
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.lists, key = { it.id }) { list ->
                    ListCard(
                        list = list,
                        onClick = { onOpenList(list.id, list.name) },
                        onDelete = { viewModel.onDeleteList(list.id) }
                    )
                }
            }
        }

        if (uiState.isCreateDialogOpen) {
            CreateListDialog(
                name = uiState.newListName,
                onNameChange = viewModel::onNewListNameChange,
                onConfirm = viewModel::onConfirmCreateList,
                onDismiss = viewModel::onDismissCreateDialog
            )
        }
    }
}
