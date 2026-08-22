package com.geo.cartwise.domain.repository

import com.geo.cartwise.domain.model.GroceryItem
import kotlinx.coroutines.flow.Flow

/**
 * The domain layer only knows about this interface, never the Room implementation.
 * That's what lets us swap the data source later (e.g. add Firebase sync) without
 * touching a single use-case or screen.
 */
interface GroceryRepository {
    fun observeItems(): Flow<List<GroceryItem>>
    suspend fun addItem(name: String)
    suspend fun setChecked(id: Long, isChecked: Boolean)
    suspend fun deleteItem(id: Long)
}
