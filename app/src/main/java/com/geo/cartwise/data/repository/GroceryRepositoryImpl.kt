package com.geo.cartwise.data.repository

import com.geo.cartwise.data.local.dao.GroceryItemDao
import com.geo.cartwise.data.local.entity.GroceryItemEntity
import com.geo.cartwise.domain.model.GroceryItem
import com.geo.cartwise.domain.repository.GroceryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroceryRepositoryImpl(
    private val dao: GroceryItemDao
) : GroceryRepository {

    override fun observeItems(): Flow<List<GroceryItem>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addItem(name: String) {
        dao.insert(
            GroceryItemEntity(
                name = name,
                isChecked = false,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun setChecked(id: Long, isChecked: Boolean) {
        dao.setChecked(id, isChecked)
    }

    override suspend fun deleteItem(id: Long) {
        dao.delete(id)
    }
}

private fun GroceryItemEntity.toDomain() = GroceryItem(
    id = id,
    name = name,
    isChecked = isChecked,
    createdAt = createdAt
)
