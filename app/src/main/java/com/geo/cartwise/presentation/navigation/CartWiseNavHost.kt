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
import com.geo.cartwise.presentation.list.GroceryListScreen
import com.geo.cartwise.presentation.list.GroceryListViewModel
import com.geo.cartwise.presentation.lists.ListsScreen
import com.geo.cartwise.presentation.lists.ListsViewModel

private const val ROUTE_LISTS = "lists"
private const val ROUTE_LIST = "list/{listId}/{listName}"

private fun listRoute(listId: Long, listName: String): String {
    val encodedName = Uri.encode(listName)
    return "list/$listId/$encodedName"
}

/**
 * Two-screen nav graph: [ListsScreen] (home) -> [GroceryListScreen] (one list's
 * items). The list's name travels in the route so the item screen never needs
 * to re-query it.
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
                }
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
                    deleteGroceryItem = container.deleteGroceryItemUseCase
                )
            )
            GroceryListScreen(
                listName = listName,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
