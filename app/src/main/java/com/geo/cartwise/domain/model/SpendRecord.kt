package com.geo.cartwise.domain.model

/**
 * One row in the monthly spend breakdown: how much was estimated to be spent
 * on a particular aisle in a given month.
 * [month] is formatted "YYYY-MM" (e.g. "2026-08").
 */
data class SpendRecord(
    val month: String,
    val aisle: String,
    val total: Double
)
