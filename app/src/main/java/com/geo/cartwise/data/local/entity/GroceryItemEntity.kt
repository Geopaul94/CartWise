package com.geo.cartwise.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * The Room-facing table row. Annotations like @Entity live only here — the rest
 * of the app (domain, UI) never imports androidx.room, so a DB swap later stays
 * contained to the data layer.
 *
 * [listId] ties an item to its parent list; CASCADE means deleting a list also
 * deletes its items (there's no orphaned-item state to worry about in the UI).
 */
@Entity(
    tableName = "grocery_items",
    foreignKeys = [
        ForeignKey(
            entity = GroceryListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listId")]
)
data class GroceryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val listId: Long,
    val name: String,
    val isChecked: Boolean,
    val aisle: String,
    val createdAt: Long
)
