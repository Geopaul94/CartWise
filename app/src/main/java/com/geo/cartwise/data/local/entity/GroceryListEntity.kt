package com.geo.cartwise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One row per shopping list — e.g. "Supermarket", "Chemist", "Wholesale Club".
 * Items belong to a list via [GroceryItemEntity.listId].
 */
@Entity(tableName = "grocery_lists")
data class GroceryListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val budget: Double = 0.0,
    val createdAt: Long
)
