package com.geo.cartwise.presentation.list

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.geo.cartwise.presentation.common.components.EmptyState
import com.geo.cartwise.presentation.list.components.AddItemBar
import com.geo.cartwise.presentation.list.components.AisleHeader
import com.geo.cartwise.presentation.list.components.BudgetSummaryBar
import com.geo.cartwise.presentation.list.components.GroceryItemRow
import com.geo.cartwise.presentation.list.components.SetBudgetDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GroceryListScreen(
    listName: String,
    viewModel: GroceryListViewModel,
    onBack: () -> Unit,
    onScanClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // System speech dialog handles its own audio; no RECORD_AUDIO permission needed.
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.onVoiceResult(spokenText)
            }
        }
    }

    val isEmpty = uiState.aisleGroups.isEmpty() && uiState.checkedItems.isEmpty()

    if (uiState.showSetBudgetDialog) {
        SetBudgetDialog(
            currentBudget = uiState.budget,
            onConfirm = viewModel::onBudgetConfirmed,
            onDismiss = viewModel::onDismissBudgetDialog
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(listName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::onSetBudgetClick) {
                        Icon(Icons.Filled.AttachMoney, contentDescription = "Set budget")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        bottomBar = {
            AddItemBar(
                value = uiState.inputText,
                onValueChange = viewModel::onInputChange,
                priceValue = uiState.priceInput,
                onPriceChange = viewModel::onPriceChange,
                onSubmit = viewModel::onAddItem,
                onScanClick = onScanClick,
                onVoiceClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Say items, e.g. \"eggs and bread\"")
                    }
                    try {
                        voiceLauncher.launch(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "Voice input isn't available on this device", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isEmpty) {
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
                // Budget summary strip — only shown when a budget is set.
                if (uiState.budget > 0.0) {
                    item(key = "budget_bar") {
                        BudgetSummaryBar(
                            totalEstimatedPrice = uiState.totalEstimatedPrice,
                            budget = uiState.budget
                        )
                    }
                }

                // Unchecked items grouped by aisle with sticky section headers.
                for (group in uiState.aisleGroups) {
                    stickyHeader(key = "header_${group.aisle}") {
                        AisleHeader(label = group.aisle)
                    }
                    items(group.items, key = { it.id }) { item ->
                        GroceryItemRow(
                            item = item,
                            onCheckedChange = { checked -> viewModel.onToggleChecked(item.id, checked) },
                            onDelete = { viewModel.onDeleteItem(item.id) }
                        )
                    }
                }

                // Checked items sink to bottom under a muted header.
                if (uiState.checkedItems.isNotEmpty()) {
                    stickyHeader(key = "header_checked") {
                        AisleHeader(
                            label = "Checked (${uiState.checkedItems.size})",
                            muted = true
                        )
                    }
                    items(uiState.checkedItems, key = { "checked_${it.id}" }) { item ->
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
}
