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

    @Query("SELECT budget FROM grocery_lists WHERE id = :id")
    fun observeBudget(id: Long): Flow<Double>

    @Insert
    suspend fun insert(list: GroceryListEntity): Long

    @Query("UPDATE grocery_lists SET budget = :budget WHERE id = :id")
    suspend fun setBudget(id: Long, budget: Double)

    @Query("SELECT name FROM grocery_lists WHERE id = :id LIMIT 1")
    suspend fun getNameById(id: Long): String?

    @Query("DELETE FROM grocery_lists WHERE id = :id")
    suspend fun delete(id: Long)
}
