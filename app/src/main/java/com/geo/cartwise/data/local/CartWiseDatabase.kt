package com.geo.cartwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geo.cartwise.data.local.dao.GroceryItemDao
import com.geo.cartwise.data.local.entity.GroceryItemEntity

@Database(
    entities = [GroceryItemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class CartWiseDatabase : RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao

    companion object {
        const val DATABASE_NAME = "cartwise.db"
    }
}
