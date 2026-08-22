package com.geo.cartwise.presentation.barcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.geo.cartwise.domain.usecase.AddGroceryItemUseCase
import com.geo.cartwise.domain.usecase.LookupProductUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Owns the scan -> lookup -> add-to-list flow. Camera/permission plumbing is
 * platform UI concern and stays in the screen; this class only knows about a
 * barcode string coming in and an item being added.
 */
class BarcodeScanViewModel(
    private val listId: Long,
    private val lookupProduct: LookupProductUseCase,
    private val addGroceryItem: AddGroceryItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BarcodeScanUiState())
    val uiState: StateFlow<BarcodeScanUiState> = _uiState.asStateFlow()

    private val _itemAddedEvents = MutableSharedFlow<String>()
    val itemAddedEvents: SharedFlow<String> = _itemAddedEvents

    // Guards against ImageAnalysis calling us multiple times per second while
    // a lookup for the same barcode is already in flight.
    private var isHandlingBarcode = false

    fun onBarcodeDetected(barcode: String) {
        if (isHandlingBarcode) return
        isHandlingBarcode = true
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val product = lookupProduct(barcode)
            if (product != null) {
                addGroceryItem(listId, product.name)
                _uiState.update { it.copy(isLoading = false) }
                _itemAddedEvents.emit(product.name)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Couldn't find that product. Try again or add it manually."
                    )
                }
                isHandlingBarcode = false
            }
        }
    }

    fun onDismissError() {
        _uiState.update { it.copy(errorMessage = null) }
        isHandlingBarcode = false
    }

    class Factory(
        private val listId: Long,
        private val lookupProduct: LookupProductUseCase,
        private val addGroceryItem: AddGroceryItemUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BarcodeScanViewModel(listId, lookupProduct, addGroceryItem) as T
        }
    }
}
