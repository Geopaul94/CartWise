package com.geo.cartwise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The Room-facing table row. Annotations like @Entity live only here — the rest
 * of the app (domain, UI) never imports androidx.room, so a DB swap later stays
 * contained to the data layer.
 */
@Entity(tableName = "grocery_items")
data class GroceryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val isChecked: Boolean,
    val createdAt: Long
)
