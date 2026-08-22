package com.geo.cartwise.di

import android.content.Context
import androidx.room.Room
import com.geo.cartwise.data.local.CartWiseDatabase
import com.geo.cartwise.data.repository.GroceryRepositoryImpl
import com.geo.cartwise.domain.repository.GroceryRepository
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import com.geo.cartwise.domain.usecase.DeleteGroceryItemUseCase
import com.geo.cartwise.domain.usecase.ObserveGroceryItemsUseCase
import com.geo.cartwise.domain.usecase.SetItemCheckedUseCase

/**
 * Manual dependency injection: one place that wires "real" implementations
 * (Room, repository) into the interfaces the rest of the app depends on.
 * No Hilt/Dagger yet — for a small app this stays easy to follow, and we can
 * graduate to Hilt later once the dependency graph actually gets complex.
 */
class AppContainer(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        CartWiseDatabase::class.java,
        CartWiseDatabase.DATABASE_NAME
    ).build()

    private val groceryRepository: GroceryRepository =
        GroceryRepositoryImpl(database.groceryItemDao())

    val observeGroceryItemsUseCase = ObserveGroceryItemsUseCase(groceryRepository)
    val addGroceryItemUseCase = AddGroceryItemUseCase(groceryRepository)
    val setItemCheckedUseCase = SetItemCheckedUseCase(groceryRepository)
    val deleteGroceryItemUseCase = DeleteGroceryItemUseCase(groceryRepository)
}
