package com.geo.cartwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.geo.cartwise.data.local.entity.GroceryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryItemDao {
    // Unchecked items first (newest first), then checked items sink to the
    // bottom — matches the "checked items fade and move to bottom" UX direction.
    @Query("SELECT * FROM grocery_items ORDER BY isChecked ASC, createdAt DESC")
    fun observeAll(): Flow<List<GroceryItemEntity>>

    @Insert
    suspend fun insert(item: GroceryItemEntity)

    @Query("UPDATE grocery_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun setChecked(id: Long, isChecked: Boolean)

    @Query("DELETE FROM grocery_items WHERE id = :id")
    suspend fun delete(id: Long)
}
