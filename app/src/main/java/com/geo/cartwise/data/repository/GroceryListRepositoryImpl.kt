package com.geo.cartwise.data.repository

import com.geo.cartwise.data.local.dao.GroceryItemDao
import com.geo.cartwise.data.local.dao.GroceryListDao
import com.geo.cartwise.data.local.entity.GroceryListEntity
import com.geo.cartwise.domain.model.GroceryList
import com.geo.cartwise.domain.repository.GroceryListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GroceryListRepositoryImpl(
    private val listDao: GroceryListDao,
    private val itemDao: GroceryItemDao
) : GroceryListRepository {

    override fun observeLists(): Flow<List<GroceryList>> =
        combine(listDao.observeAll(), itemDao.observeRemainingCounts()) { lists, counts ->
            val countByListId = counts.associate { it.listId to it.count }
            lists.map { list ->
                GroceryList(
                    id = list.id,
                    name = list.name,
                    createdAt = list.createdAt,
                    remainingCount = countByListId[list.id] ?: 0,
                    budget = list.budget
                )
            }
        }

    override fun observeListBudget(id: Long): Flow<Double> = listDao.observeBudget(id)

    override suspend fun createList(name: String): Long =
        listDao.insert(GroceryListEntity(name = name, createdAt = System.currentTimeMillis()))

    override suspend fun setListBudget(id: Long, budget: Double) {
        listDao.setBudget(id, budget)
    }

    override suspend fun deleteList(id: Long) {
        listDao.delete(id)
    }
}
