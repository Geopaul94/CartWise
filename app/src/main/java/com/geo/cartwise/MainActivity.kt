package com.geo.cartwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geo.cartwise.presentation.list.GroceryListScreen
import com.geo.cartwise.presentation.list.GroceryListViewModel
import com.geo.cartwise.presentation.theme.CartWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CartWiseApp).container
        val viewModelFactory = GroceryListViewModel.Factory(
            observeGroceryItems = container.observeGroceryItemsUseCase,
            addGroceryItem = container.addGroceryItemUseCase,
            setItemChecked = container.setItemCheckedUseCase,
            deleteGroceryItem = container.deleteGroceryItemUseCase
        )

        setContent {
            CartWiseTheme {
                val viewModel: GroceryListViewModel = viewModel(factory = viewModelFactory)
                GroceryListScreen(viewModel = viewModel)
            }
        }
    }
}
