package com.geo.cartwise.domain.model

/**
 * The domain-layer shape of a grocery item — no Room/DB annotations here.
 * Keeping this separate from the Room entity means the UI and use-cases never
 * depend on how data is stored, only on this plain model.
 */
data class GroceryItem(
    val id: Long = 0L,
    val name: String,
    val isChecked: Boolean = false,
    val aisle: String = Aisle.OTHER,
    val estimatedPrice: Double = 0.0,
    val createdAt: Long
)
