package com.geo.cartwise.presentation.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.geo.cartwise.domain.usecase.CreateGroceryListUseCase
import com.geo.cartwise.domain.usecase.DeleteGroceryListUseCase
import com.geo.cartwise.domain.usecase.ObserveGroceryListsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListsViewModel(
    private val observeGroceryLists: ObserveGroceryListsUseCase,
    private val createGroceryList: CreateGroceryListUseCase,
    private val deleteGroceryList: DeleteGroceryListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListsUiState())
    val uiState: StateFlow<ListsUiState> = _uiState.asStateFlow()

    // One-shot navigation signal: fires the new list's id + name once created,
    // so the screen can navigate straight into it without stashing "pending
    // navigation" in persistent UI state or racing the DB's own list flow.
    private val _openListEvents = MutableSharedFlow<OpenListEvent>()
    val openListEvents: SharedFlow<OpenListEvent> = _openListEvents

    init {
        viewModelScope.launch {
            observeGroceryLists().collect { lists ->
                _uiState.update { it.copy(lists = lists) }
            }
        }
    }

    fun onCreateListClick() {
        _uiState.update { it.copy(isCreateDialogOpen = true, newListName = "") }
    }

    fun onDismissCreateDialog() {
        _uiState.update { it.copy(isCreateDialogOpen = false) }
    }

    fun onNewListNameChange(text: String) {
        _uiState.update { it.copy(newListName = text) }
    }

    fun onConfirmCreateList() {
        val name = _uiState.value.newListName
        viewModelScope.launch {
            val newListId = createGroceryList(name)
            _uiState.update { it.copy(isCreateDialogOpen = false, newListName = "") }
            if (newListId != null) {
                _openListEvents.emit(OpenListEvent(newListId, name.trim()))
            }
        }
    }

    fun onDeleteList(id: Long) {
        viewModelScope.launch { deleteGroceryList(id) }
    }

    class Factory(
        private val observeGroceryLists: ObserveGroceryListsUseCase,
        private val createGroceryList: CreateGroceryListUseCase,
        private val deleteGroceryList: DeleteGroceryListUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListsViewModel(observeGroceryLists, createGroceryList, deleteGroceryList) as T
        }
    }
}

data class OpenListEvent(val listId: Long, val listName: String)
