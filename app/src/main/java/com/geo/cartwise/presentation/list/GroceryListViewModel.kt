package com.geo.cartwise.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import com.geo.cartwise.domain.usecase.DeleteGroceryItemUseCase
import com.geo.cartwise.domain.usecase.ObserveGroceryItemsUseCase
import com.geo.cartwise.domain.usecase.ParseSpokenItemsUseCase
import com.geo.cartwise.domain.usecase.SetItemCheckedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Holds UI state for a single list's items and survives configuration changes
 * (screen rotation). The screen only reads [uiState] and calls these functions —
 * it never talks to a use case or the database directly.
 */
class GroceryListViewModel(
    private val listId: Long,
    private val addGroceryItem: AddGroceryItemUseCase,
    private val setItemChecked: SetItemCheckedUseCase,
    private val deleteGroceryItem: DeleteGroceryItemUseCase,
    private val parseSpokenItems: ParseSpokenItemsUseCase,
    observeGroceryItems: ObserveGroceryItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroceryListUiState())
    val uiState: StateFlow<GroceryListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeGroceryItems(listId).collect { items ->
                _uiState.update { it.copy(items = items) }
            }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onAddItem() {
        val name = _uiState.value.inputText
        if (name.isBlank()) return
        viewModelScope.launch {
            addGroceryItem(listId, name)
            _uiState.update { it.copy(inputText = "") }
        }
    }

    /** "eggs and bread" from the mic becomes two items in one go. */
    fun onVoiceResult(spokenText: String) {
        val names = parseSpokenItems(spokenText)
        if (names.isEmpty()) return
        viewModelScope.launch {
            names.forEach { name -> addGroceryItem(listId, name) }
        }
    }

    fun onToggleChecked(id: Long, isChecked: Boolean) {
        viewModelScope.launch { setItemChecked(id, isChecked) }
    }

    fun onDeleteItem(id: Long) {
        viewModelScope.launch { deleteGroceryItem(id) }
    }

    class Factory(
        private val listId: Long,
        private val observeGroceryItems: ObserveGroceryItemsUseCase,
        private val addGroceryItem: AddGroceryItemUseCase,
        private val setItemChecked: SetItemCheckedUseCase,
        private val deleteGroceryItem: DeleteGroceryItemUseCase,
        private val parseSpokenItems: ParseSpokenItemsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroceryListViewModel(
                listId,
                addGroceryItem,
                setItemChecked,
                deleteGroceryItem,
                parseSpokenItems,
                observeGroceryItems
            ) as T
        }
    }
}
