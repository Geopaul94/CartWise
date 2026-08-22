package com.geo.cartwise.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.geo.cartwise.domain.model.SpendRecord
import com.geo.cartwise.domain.usecase.ObserveSpendHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SpendHistoryViewModel(
    private val observeSpendHistory: ObserveSpendHistoryUseCase
) : ViewModel() {

    // All records cached here so month-switching can re-filter without a DB round-trip.
    private var allRecords: List<SpendRecord> = emptyList()

    private val _uiState = MutableStateFlow(SpendHistoryUiState())
    val uiState: StateFlow<SpendHistoryUiState> = _uiState.asStateFlow()

    init {
        observeSpendHistory().onEach { records ->
            allRecords = records
            applyFilter(currentMonth = _uiState.value.selectedMonth, records = records)
        }.launchIn(viewModelScope)
    }

    fun onSelectMonth(month: String) {
        applyFilter(currentMonth = month, records = allRecords)
    }

    private fun applyFilter(currentMonth: String, records: List<SpendRecord>) {
        val months = records.map { it.month }.distinct().sortedDescending().take(6)
        val selected = if (currentMonth in months) currentMonth else months.firstOrNull().orEmpty()
        val filtered = records.filter { it.month == selected }.sortedByDescending { it.total }

        _uiState.update {
            it.copy(
                availableMonths = months,
                selectedMonth = selected,
                categoryRows = filtered,
                grandTotal = filtered.sumOf { r -> r.total },
                isEmpty = months.isEmpty()
            )
        }
    }

    class Factory(
        private val observeSpendHistory: ObserveSpendHistoryUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SpendHistoryViewModel(observeSpendHistory) as T
    }
}
