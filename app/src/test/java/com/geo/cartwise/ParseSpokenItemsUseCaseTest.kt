package com.geo.cartwise

import com.geo.cartwise.domain.usecase.ParseSpokenItemsUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class ParseSpokenItemsUseCaseTest {

    private val useCase = ParseSpokenItemsUseCase()

    @Test
    fun `splits on the word and`() {
        assertEquals(listOf("eggs", "bread"), useCase("eggs and bread"))
    }

    @Test
    fun `splits on commas and ampersands`() {
        assertEquals(listOf("milk", "bread", "butter"), useCase("milk, bread & butter"))
    }

    @Test
    fun `single item returns one entry`() {
        assertEquals(listOf("milk"), useCase("milk"))
    }

    @Test
    fun `blank segments are dropped`() {
        assertEquals(listOf("eggs", "bread"), useCase("eggs, , and bread"))
    }
}
