package com.geo.cartwise

import com.geo.cartwise.domain.model.GroceryItem
import com.geo.cartwise.domain.repository.GroceryRepository
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeGroceryRepository : GroceryRepository {
    val addedNames = mutableListOf<String>()
    private val itemsFlow = MutableStateFlow<List<GroceryItem>>(emptyList())

    override fun observeItems(): StateFlow<List<GroceryItem>> = itemsFlow
    override suspend fun addItem(name: String) { addedNames.add(name) }
    override suspend fun setChecked(id: Long, isChecked: Boolean) {}
    override suspend fun deleteItem(id: Long) {}
}

class AddGroceryItemUseCaseTest {

    @Test
    fun `blank input is not added`() = runBlocking {
        val repository = FakeGroceryRepository()
        val useCase = AddGroceryItemUseCase(repository)

        useCase("   ")

        assertEquals(0, repository.addedNames.size)
    }

    @Test
    fun `input is trimmed before saving`() = runBlocking {
        val repository = FakeGroceryRepository()
        val useCase = AddGroceryItemUseCase(repository)

        useCase("  milk  ")

        assertEquals("milk", repository.addedNames.single())
    }
}
