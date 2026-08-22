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
    @Query("SELECT * FROM grocery_items WHERE listId = :listId ORDER BY isChecked ASC, aisle ASC, createdAt DESC")
    fun observeByList(listId: Long): Flow<List<GroceryItemEntity>>

    // One grouped query instead of one flow per list — stays cheap as the
    // number of lists grows.
    @Query("SELECT listId, COUNT(*) as count FROM grocery_items WHERE isChecked = 0 GROUP BY listId")
    fun observeRemainingCounts(): Flow<List<RemainingCount>>

    @Insert
    suspend fun insert(item: GroceryItemEntity)

    @Query("UPDATE grocery_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun setChecked(id: Long, isChecked: Boolean)

    @Query("UPDATE grocery_items SET estimatedPrice = :price WHERE id = :id")
    suspend fun setPrice(id: Long, price: Double)

    @Query("UPDATE grocery_items SET isChecked = 0 WHERE listId = :listId")
    suspend fun uncheckAll(listId: Long)

    @Query("SELECT * FROM grocery_items WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): GroceryItemEntity?

    // Returns up to 5 distinct item names that start with the query string,
    // most-recently added first — used for the autosuggest dropdown.
    @Query("""
        SELECT DISTINCT name FROM grocery_items
        WHERE name LIKE :query || '%'
        ORDER BY createdAt DESC
        LIMIT 5
    """)
    suspend fun getSuggestedNames(query: String): List<String>

    // Returns every unchecked item for a list; used by the home-screen widget
    // to show what still needs to be bought.
    @Query("SELECT * FROM grocery_items WHERE listId = :listId AND isChecked = 0 ORDER BY aisle ASC, name ASC")
    suspend fun getUncheckedByList(listId: Long): List<GroceryItemEntity>

    // The list with the most unchecked items is shown in the widget.
    @Query("""
        SELECT listId, COUNT(*) as count
        FROM grocery_items
        WHERE isChecked = 0
        GROUP BY listId
        ORDER BY count DESC
        LIMIT 1
    """)
    suspend fun getListIdWithMostUnchecked(): RemainingCount?

    @Query("DELETE FROM grocery_items WHERE id = :id")
    suspend fun delete(id: Long)

    // Groups checked items by month (YYYY-MM) and aisle so SpendHistoryScreen
    // can show a monthly breakdown without a separate table.
    @Query("""
        SELECT
            strftime('%Y-%m', createdAt / 1000, 'unixepoch') AS month,
            aisle,
            SUM(estimatedPrice) AS total
        FROM grocery_items
        WHERE isChecked = 1 AND estimatedPrice > 0
        GROUP BY month, aisle
        ORDER BY month DESC
    """)
    fun observeSpendHistory(): Flow<List<AisleSpend>>
}

data class RemainingCount(val listId: Long, val count: Int)
data class AisleSpend(val month: String, val aisle: String, val total: Double)
