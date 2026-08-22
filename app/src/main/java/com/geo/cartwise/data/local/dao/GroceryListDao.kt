package com.geo.cartwise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.geo.cartwise.data.local.entity.GroceryListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryListDao {
    @Query("SELECT * FROM grocery_lists ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<GroceryListEntity>>

    @Insert
    suspend fun insert(list: GroceryListEntity): Long

    @Query("DELETE FROM grocery_lists WHERE id = :id")
    suspend fun delete(id: Long)
}
