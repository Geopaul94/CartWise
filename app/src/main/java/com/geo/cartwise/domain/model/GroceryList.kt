package com.geo.cartwise.domain.model

data class GroceryList(
    val id: Long = 0L,
    val name: String,
    val createdAt: Long,
    val remainingCount: Int = 0
)
