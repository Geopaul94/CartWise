package com.geo.cartwise

import com.geo.cartwise.domain.model.GroceryItem
import com.geo.cartwise.domain.repository.GroceryItemRepository
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeGroceryItemRepository : GroceryItemRepository {
    val addedNames = mutableListOf<String>()
    private val itemsFlow = MutableStateFlow<List<GroceryItem>>(emptyList())

    override fun observeItems(listId: Long): StateFlow<List<GroceryItem>> = itemsFlow
    override suspend fun addItem(listId: Long, name: String) { addedNames.add(name) }
    override suspend fun setChecked(id: Long, isChecked: Boolean) {}
    override suspend fun deleteItem(id: Long) {}
}

class AddGroceryItemUseCaseTest {

    @Test
    fun `blank input is not added`() = runBlocking {
        val repository = FakeGroceryItemRepository()
        val useCase = AddGroceryItemUseCase(repository)

        useCase(listId = 1L, name = "   ")

        assertEquals(0, repository.addedNames.size)
    }

    @Test
    fun `input is trimmed before saving`() = runBlocking {
        val repository = FakeGroceryItemRepository()
        val useCase = AddGroceryItemUseCase(repository)

        useCase(listId = 1L, name = "  milk  ")

        assertEquals("milk", repository.addedNames.single())
    }
}
