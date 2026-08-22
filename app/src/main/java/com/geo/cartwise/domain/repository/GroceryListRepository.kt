package com.geo.cartwise.domain.repository

import com.geo.cartwise.domain.model.GroceryList
import kotlinx.coroutines.flow.Flow

interface GroceryListRepository {
    fun observeLists(): Flow<List<GroceryList>>
    suspend fun createList(name: String): Long
    suspend fun deleteList(id: Long)
}
