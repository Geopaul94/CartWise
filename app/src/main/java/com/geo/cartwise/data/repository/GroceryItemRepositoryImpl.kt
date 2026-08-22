package com.geo.cartwise.data.repository

import com.geo.cartwise.data.local.dao.GroceryItemDao
import com.geo.cartwise.data.local.entity.GroceryItemEntity
import com.geo.cartwise.domain.model.GroceryItem
import com.geo.cartwise.domain.repository.GroceryItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroceryItemRepositoryImpl(
    private val dao: GroceryItemDao
) : GroceryItemRepository {

    override fun observeItems(listId: Long): Flow<List<GroceryItem>> =
        dao.observeByList(listId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun addItem(listId: Long, name: String, aisle: String) {
        dao.insert(
            GroceryItemEntity(
                listId = listId,
                name = name,
                isChecked = false,
                aisle = aisle,
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
    aisle = aisle,
    createdAt = createdAt
)
