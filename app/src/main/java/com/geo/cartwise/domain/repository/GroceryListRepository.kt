package com.geo.cartwise.domain.repository

import com.geo.cartwise.domain.model.GroceryList
import kotlinx.coroutines.flow.Flow

interface GroceryListRepository {
    fun observeLists(): Flow<List<GroceryList>>
    fun observeListBudget(id: Long): Flow<Double>
    suspend fun createList(name: String): Long
    suspend fun setListBudget(id: Long, budget: Double)
    suspend fun deleteList(id: Long)
}
