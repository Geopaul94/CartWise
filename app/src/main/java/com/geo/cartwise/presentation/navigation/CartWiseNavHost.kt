package com.geo.cartwise.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.geo.cartwise.di.AppContainer
import com.geo.cartwise.presentation.barcode.BarcodeScanViewModel
import com.geo.cartwise.presentation.barcode.BarcodeScannerScreen
import com.geo.cartwise.presentation.history.SpendHistoryScreen
import com.geo.cartwise.presentation.history.SpendHistoryViewModel
import com.geo.cartwise.presentation.list.GroceryListScreen
import com.geo.cartwise.presentation.list.GroceryListViewModel
import com.geo.cartwise.presentation.lists.ListsScreen
import com.geo.cartwise.presentation.lists.ListsViewModel

private const val ROUTE_LISTS = "lists"
private const val ROUTE_LIST = "list/{listId}/{listName}"
private const val ROUTE_SCAN = "scan/{listId}"
private const val ROUTE_HISTORY = "spend_history"

private fun listRoute(listId: Long, listName: String): String {
    val encodedName = Uri.encode(listName)
    return "list/$listId/$encodedName"
}

private fun scanRoute(listId: Long): String = "scan/$listId"

/**
 * Nav graph: [ListsScreen] (home) -> [GroceryListScreen] (one list's items)
 * -> [BarcodeScannerScreen] (scan into that same list). The list's name
 * travels in the route so downstream screens never need to re-query it.
 */
@Composable
fun CartWiseNavHost(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_LISTS) {
        composable(ROUTE_LISTS) {
            val viewModel: ListsViewModel = viewModel(
                factory = ListsViewModel.Factory(
                    observeGroceryLists = container.observeGroceryListsUseCase,
                    createGroceryList = container.createGroceryListUseCase,
                    deleteGroceryList = container.deleteGroceryListUseCase
                )
            )
            ListsScreen(
                viewModel = viewModel,
                onOpenList = { listId, listName ->
                    navController.navigate(listRoute(listId, listName))
                },
                onOpenHistory = { navController.navigate(ROUTE_HISTORY) }
            )
        }
        composable(ROUTE_HISTORY) {
            val viewModel: SpendHistoryViewModel = viewModel(
                factory = SpendHistoryViewModel.Factory(container.observeSpendHistoryUseCase)
            )
            SpendHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ROUTE_LIST,
            arguments = listOf(
                navArgument("listId") { type = NavType.LongType },
                navArgument("listName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            val listName = backStackEntry.arguments?.getString("listName").orEmpty()
            val viewModel: GroceryListViewModel = viewModel(
                factory = GroceryListViewModel.Factory(
                    listId = listId,
                    observeGroceryItems = container.observeGroceryItemsUseCase,
                    addGroceryItem = container.addGroceryItemUseCase,
                    setItemChecked = container.setItemCheckedUseCase,
                    deleteGroceryItem = container.deleteGroceryItemUseCase,
                    parseSpokenItems = container.parseSpokenItemsUseCase,
                    observeListBudget = container.observeListBudgetUseCase,
                    setListBudget = container.setListBudgetUseCase,
                    restockList = container.restockListUseCase
                )
            )
            GroceryListScreen(
                listName = listName,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onScanClick = { navController.navigate(scanRoute(listId)) }
            )
        }
        composable(
            route = ROUTE_SCAN,
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            val viewModel: BarcodeScanViewModel = viewModel(
                factory = BarcodeScanViewModel.Factory(
                    listId = listId,
                    lookupProduct = container.lookupProductUseCase,
                    addGroceryItem = container.addGroceryItemUseCase
                )
            )
            BarcodeScannerScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
