package com.geo.cartwise.presentation.history

import com.geo.cartwise.domain.model.SpendRecord

/**
 * Everything SpendHistoryScreen needs to render itself.
 * [availableMonths] drives the MonthPicker (most-recent first).
 * [selectedMonth] is the one the user has picked; "" means no data yet.
 * [categoryRows] are the spend records for that selected month.
 * [grandTotal] is the sum across all aisles for the selected month.
 */
data class SpendHistoryUiState(
    val availableMonths: List<String> = emptyList(),
    val selectedMonth: String = "",
    val categoryRows: List<SpendRecord> = emptyList(),
    val grandTotal: Double = 0.0,
    val isEmpty: Boolean = true
)
