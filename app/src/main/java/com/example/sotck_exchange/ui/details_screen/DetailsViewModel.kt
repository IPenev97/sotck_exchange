package com.example.sotck_exchange.ui.details_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sotck_exchange.domain.IStockRepository
import com.example.sotck_exchange.domain.dto.StockDTO
import com.example.sotck_exchange.domain.dto.toModel
import com.example.stock_exchange.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: IStockRepository
) : ViewModel() {

    private val stockId: Long = savedStateHandle["stockId"] ?: 0


    private val _state = mutableStateOf(DetailsScreenState())
    val state: State<DetailsScreenState> = _state

    private var updatesJob: Job? = null
    private var connectionJob: Job? = null

    init {
        // Get initial stock from repository
        val stock = repository.getStockById(stockId)?.toModel()
        _state.value = _state.value.copy(
            stock = stock,
        )
        repository.connect()
        observeConnection()
        observeStockUpdates()
        repository.startFeed()
    }


    private fun observeConnection() {
        connectionJob = viewModelScope.launch {
            repository.connectionState.collect { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }
    }

    private fun observeStockUpdates() {
        updatesJob = viewModelScope.launch {
            repository.stockUpdates.collect { result ->
                if (result is Resource.Success && result.data.id == stockId) {
                    val updatedStockDto = result.data
                    val currentStock = _state.value.stock
                    if (currentStock != null) {
                        _state.value = _state.value.copy(
                            stock = currentStock.copy(
                                price = updatedStockDto.price,
                                goingUp = updatedStockDto.price > currentStock.price
                            )
                        )
                    }
                }
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        updatesJob?.cancel()
        connectionJob?.cancel()
    }
}
