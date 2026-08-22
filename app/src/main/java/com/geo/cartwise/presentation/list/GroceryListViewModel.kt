package com.geo.cartwise.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import com.geo.cartwise.domain.usecase.DeleteGroceryItemUseCase
import com.geo.cartwise.domain.usecase.ObserveGroceryItemsUseCase
import com.geo.cartwise.domain.usecase.ObserveListBudgetUseCase
import com.geo.cartwise.domain.usecase.ParseSpokenItemsUseCase
import com.geo.cartwise.domain.usecase.RestockListUseCase
import com.geo.cartwise.domain.usecase.SetItemCheckedUseCase
import com.geo.cartwise.domain.usecase.SetListBudgetUseCase
import com.geo.cartwise.domain.usecase.SuggestItemNamesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroceryListViewModel(
    private val listId: Long,
    private val addGroceryItem: AddGroceryItemUseCase,
    private val setItemChecked: SetItemCheckedUseCase,
    private val deleteGroceryItem: DeleteGroceryItemUseCase,
    private val parseSpokenItems: ParseSpokenItemsUseCase,
    private val setListBudget: SetListBudgetUseCase,
    private val restockList: RestockListUseCase,
    private val suggestItemNames: SuggestItemNamesUseCase,
    observeGroceryItems: ObserveGroceryItemsUseCase,
    observeListBudget: ObserveListBudgetUseCase
) : ViewModel() {

    // Holds the pending suggestion fetch so we can cancel it when the user
    // types again before the 300ms debounce fires.
    private var suggestJob: Job? = null

    private val _uiState = MutableStateFlow(GroceryListUiState())
    val uiState: StateFlow<GroceryListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGroceryItems(listId).collect { items ->
                val unchecked = items.filter { !it.isChecked }
                val checked = items.filter { it.isChecked }
                // DB already sorts by aisle ASC so groupBy preserves that order.
                val groups = unchecked
                    .groupBy { it.aisle }
                    .map { (aisle, groupItems) -> AisleGroup(aisle, groupItems) }
                val total = items.sumOf { it.estimatedPrice }
                _uiState.update {
                    it.copy(aisleGroups = groups, checkedItems = checked, totalEstimatedPrice = total)
                }
            }
        }
        viewModelScope.launch {
            observeListBudget(listId).collect { budget ->
                _uiState.update { it.copy(budget = budget) }
            }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
        // Cancel any in-flight query, then wait 300ms before firing a new one.
        // This way we only hit the DB once the user pauses, not on every keystroke.
        suggestJob?.cancel()
        suggestJob = viewModelScope.launch {
            delay(300)
            val results = suggestItemNames(text)
            _uiState.update { it.copy(suggestions = results) }
        }
    }

    fun onSuggestionSelected(name: String) {
        suggestJob?.cancel()
        // Fill the text field and clear the dropdown immediately.
        _uiState.update { it.copy(inputText = name, suggestions = emptyList()) }
    }

    fun onDismissSuggestions() {
        _uiState.update { it.copy(suggestions = emptyList()) }
    }

    fun onPriceChange(text: String) {
        // Allow only digits and a single decimal point.
        val filtered = text.filter { it.isDigit() || it == '.' }
            .let { s ->
                val dotIndex = s.indexOf('.')
                if (dotIndex == -1) s
                else s.substring(0, dotIndex + 1) + s.substring(dotIndex + 1).replace(".", "")
            }
        _uiState.update { it.copy(priceInput = filtered) }
    }

    fun onAddItem() {
        val name = _uiState.value.inputText
        if (name.isBlank()) return
        val price = _uiState.value.priceInput.toDoubleOrNull() ?: 0.0
        viewModelScope.launch {
            addGroceryItem(listId, name, price)
            _uiState.update { it.copy(inputText = "", priceInput = "", suggestions = emptyList()) }
        }
    }

    fun onVoiceResult(spokenText: String) {
        val names = parseSpokenItems(spokenText)
        if (names.isEmpty()) return
        viewModelScope.launch {
            // Voice items have no price info — user can add prices manually.
            names.forEach { name -> addGroceryItem(listId, name) }
        }
    }

    fun onToggleChecked(id: Long, isChecked: Boolean) {
        viewModelScope.launch { setItemChecked(id, isChecked) }
    }

    fun onDeleteItem(id: Long) {
        viewModelScope.launch { deleteGroceryItem(id) }
    }

    fun onSetBudgetClick() {
        _uiState.update { it.copy(showSetBudgetDialog = true) }
    }

    fun onBudgetConfirmed(amount: Double) {
        viewModelScope.launch {
            setListBudget(listId, amount)
            _uiState.update { it.copy(showSetBudgetDialog = false) }
        }
    }

    fun onDismissBudgetDialog() {
        _uiState.update { it.copy(showSetBudgetDialog = false) }
    }

    fun onRestockClick() {
        _uiState.update { it.copy(showRestockDialog = true) }
    }

    fun onRestockConfirmed() {
        viewModelScope.launch {
            restockList(listId)
            _uiState.update { it.copy(showRestockDialog = false) }
        }
    }

    fun onDismissRestockDialog() {
        _uiState.update { it.copy(showRestockDialog = false) }
    }

    class Factory(
        private val listId: Long,
        private val observeGroceryItems: ObserveGroceryItemsUseCase,
        private val addGroceryItem: AddGroceryItemUseCase,
        private val setItemChecked: SetItemCheckedUseCase,
        private val deleteGroceryItem: DeleteGroceryItemUseCase,
        private val parseSpokenItems: ParseSpokenItemsUseCase,
        private val observeListBudget: ObserveListBudgetUseCase,
        private val setListBudget: SetListBudgetUseCase,
        private val restockList: RestockListUseCase,
        private val suggestItemNames: SuggestItemNamesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroceryListViewModel(
                listId,
                addGroceryItem,
                setItemChecked,
                deleteGroceryItem,
                parseSpokenItems,
                setListBudget,
                restockList,
                suggestItemNames,
                observeGroceryItems,
                observeListBudget
            ) as T
        }
    }
}
