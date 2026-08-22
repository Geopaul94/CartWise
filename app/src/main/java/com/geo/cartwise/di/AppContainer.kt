package com.geo.cartwise.di

import android.content.Context
import androidx.room.Room
import com.geo.cartwise.data.local.CartWiseDatabase
import com.geo.cartwise.data.remote.NetworkModule
import com.geo.cartwise.data.repository.GroceryItemRepositoryImpl
import com.geo.cartwise.data.repository.GroceryListRepositoryImpl
import com.geo.cartwise.data.repository.ProductLookupRepositoryImpl
import com.geo.cartwise.domain.repository.GroceryItemRepository
import com.geo.cartwise.domain.repository.GroceryListRepository
import com.geo.cartwise.domain.repository.ProductLookupRepository
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import com.geo.cartwise.domain.usecase.CreateGroceryListUseCase
import com.geo.cartwise.domain.usecase.DeleteGroceryItemUseCase
import com.geo.cartwise.domain.usecase.DeleteGroceryListUseCase
import com.geo.cartwise.domain.usecase.LookupProductUseCase
import com.geo.cartwise.domain.usecase.ObserveGroceryItemsUseCase
import com.geo.cartwise.domain.usecase.ObserveGroceryListsUseCase
import com.geo.cartwise.domain.usecase.ObserveListBudgetUseCase
import com.geo.cartwise.domain.usecase.ParseSpokenItemsUseCase
import com.geo.cartwise.domain.usecase.RestockListUseCase
import com.geo.cartwise.domain.usecase.SetItemCheckedUseCase
import com.geo.cartwise.domain.usecase.ObserveSpendHistoryUseCase
import com.geo.cartwise.domain.usecase.SetItemPriceUseCase
import com.geo.cartwise.domain.usecase.SetListBudgetUseCase

/**
 * Manual dependency injection: one place that wires "real" implementations
 * (Room, repositories) into the interfaces the rest of the app depends on.
 * No Hilt/Dagger yet — for a small app this stays easy to follow, and we can
 * graduate to Hilt later once the dependency graph actually gets complex.
 */
class AppContainer(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        CartWiseDatabase::class.java,
        CartWiseDatabase.DATABASE_NAME
    )
        // Pre-release app, no real users yet — destructive migration is fine
        // until we ship v1 and need to preserve users' saved lists.
        .fallbackToDestructiveMigration()
        .build()

    // Exposed for the Glance widget and its action callbacks, which run outside
    // the normal ViewModel/UseCase path and need direct DAO access.
    val groceryItemDao = database.groceryItemDao()
    val groceryListDao = database.groceryListDao()

    private val groceryListRepository: GroceryListRepository =
        GroceryListRepositoryImpl(groceryListDao, groceryItemDao)

    private val groceryItemRepository: GroceryItemRepository =
        GroceryItemRepositoryImpl(groceryItemDao)

    val observeGroceryListsUseCase = ObserveGroceryListsUseCase(groceryListRepository)
    val createGroceryListUseCase = CreateGroceryListUseCase(groceryListRepository)
    val deleteGroceryListUseCase = DeleteGroceryListUseCase(groceryListRepository)
    val observeListBudgetUseCase = ObserveListBudgetUseCase(groceryListRepository)
    val setListBudgetUseCase = SetListBudgetUseCase(groceryListRepository)

    val observeGroceryItemsUseCase = ObserveGroceryItemsUseCase(groceryItemRepository)
    val addGroceryItemUseCase = AddGroceryItemUseCase(groceryItemRepository)
    val setItemCheckedUseCase = SetItemCheckedUseCase(groceryItemRepository)
    val setItemPriceUseCase = SetItemPriceUseCase(groceryItemRepository)
    val restockListUseCase = RestockListUseCase(groceryItemRepository)
    val deleteGroceryItemUseCase = DeleteGroceryItemUseCase(groceryItemRepository)
    val observeSpendHistoryUseCase = ObserveSpendHistoryUseCase(groceryItemRepository)
    val parseSpokenItemsUseCase = ParseSpokenItemsUseCase()

    private val productLookupRepository: ProductLookupRepository =
        ProductLookupRepositoryImpl(NetworkModule.createOpenFoodFactsApi())

    val lookupProductUseCase = LookupProductUseCase(productLookupRepository)
}
