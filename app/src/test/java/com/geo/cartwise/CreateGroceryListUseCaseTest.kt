package com.geo.cartwise

import com.geo.cartwise.domain.model.GroceryList
import com.geo.cartwise.domain.repository.GroceryListRepository
import com.geo.cartwise.domain.usecase.CreateGroceryListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

private class FakeGroceryListRepository : GroceryListRepository {
    val createdNames = mutableListOf<String>()
    private val listsFlow = MutableStateFlow<List<GroceryList>>(emptyList())

    override fun observeLists(): StateFlow<List<GroceryList>> = listsFlow
    override suspend fun createList(name: String): Long {
        createdNames.add(name)
        return createdNames.size.toLong()
    }
    override suspend fun deleteList(id: Long) {}
}

class CreateGroceryListUseCaseTest {

    @Test
    fun `blank name is not created`() = runBlocking {
        val repository = FakeGroceryListRepository()
        val useCase = CreateGroceryListUseCase(repository)

        val result = useCase("   ")

        assertNull(result)
        assertEquals(0, repository.createdNames.size)
    }

    @Test
    fun `name is trimmed before saving`() = runBlocking {
        val repository = FakeGroceryListRepository()
        val useCase = CreateGroceryListUseCase(repository)

        useCase("  Supermarket  ")

        assertEquals("Supermarket", repository.createdNames.single())
    }
}
