package com.geo.cartwise.domain.repository

import com.geo.cartwise.domain.model.GroceryItem
import kotlinx.coroutines.flow.Flow

/**
 * The domain layer only knows about this interface, never the Room implementation.
 * That's what lets us swap the data source later (e.g. add Firebase sync) without
 * touching a single use-case or screen. Every item belongs to a list, so all
 * operations here are scoped by listId.
 */
interface GroceryItemRepository {
    fun observeItems(listId: Long): Flow<List<GroceryItem>>
    suspend fun addItem(listId: Long, name: String, aisle: String, estimatedPrice: Double)
    suspend fun setChecked(id: Long, isChecked: Boolean)
    suspend fun setItemPrice(id: Long, price: Double)
    suspend fun deleteItem(id: Long)
}
