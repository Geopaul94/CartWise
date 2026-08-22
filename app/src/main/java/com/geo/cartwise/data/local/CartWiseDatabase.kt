package com.geo.cartwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geo.cartwise.data.local.dao.GroceryItemDao
import com.geo.cartwise.data.local.dao.GroceryListDao
import com.geo.cartwise.data.local.entity.GroceryItemEntity
import com.geo.cartwise.data.local.entity.GroceryListEntity

@Database(
    entities = [GroceryListEntity::class, GroceryItemEntity::class],
    version = 3,
    exportSchema = true
)
abstract class CartWiseDatabase : RoomDatabase() {
    abstract fun groceryListDao(): GroceryListDao
    abstract fun groceryItemDao(): GroceryItemDao

    companion object {
        const val DATABASE_NAME = "cartwise.db"
    }
}
