package com.example.stock_exchange.ui.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sotck_exchange.domain.IStockRepository
import com.example.sotck_exchange.domain.model.Stock
import com.example.sotck_exchange.domain.dto.StockDTO
import com.example.sotck_exchange.domain.dto.toModel
import com.example.stock_exchange.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IStockRepository
) : ViewModel() {

    private val _state = mutableStateOf(MainScreenState())
    val state: State<MainScreenState> = _state

    private var connectionJob: Job? = null
    private var updatesJob: Job? = null

    init {
        _state.value = state.value.copy(stocks = repository.getAllStocks().map{it.toModel()})
        connect()
    }

    /** Start WebSocket connection and manage reconnection automatically */
    private fun connect() {
        repository.connect()

        connectionJob = viewModelScope.launch {
            repository.connectionState.collect { isConnected ->
                _state.value = _state.value.copy(
                    isConnected = isConnected,
                    isFeedRunning = _state.value.isFeedRunning && isConnected
                )

                if (!isConnected) stopFeedUpdates()
            }
        }

        updatesJob = viewModelScope.launch {
            repository.stockUpdates.collect { result ->
                if (result is Resource.Success) {
                    val updatedStockDto = result.data
                    val updatedStocks = _state.value.stocks.map { stock ->
                        if (stock.id == updatedStockDto.id) {
                            val goingUp = updatedStockDto.price > stock.price // last known price
                            stock.copy(
                                price = updatedStockDto.price,
                                goingUp = goingUp
                            )
                        } else stock
                    }.sortedByDescending { it.price }

                    _state.value = _state.value.copy(stocks = updatedStocks)
                }
            }
        }
    }

    /** Start feed updates via repository if connected */
    private fun startFeedUpdates() {
        if (!_state.value.isConnected) return
        repository.startFeed()
    }

    /** Stop feed updates */
    private fun stopFeedUpdates() {
        repository.stopFeed()
    }

    /** Handle UI events */
    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.ToggleFeed -> {
                if (!_state.value.isConnected) return

                val newFeedState = !_state.value.isFeedRunning
                _state.value = _state.value.copy(isFeedRunning = newFeedState)

                if (newFeedState) startFeedUpdates() else stopFeedUpdates()
            }
        }
    }
    fun syncFeedState(){
        if(_state.value.isFeedRunning){
            repository.startFeed()
        } else {
            repository.stopFeed()
        }
    }

    override fun onCleared() {
        super.onCleared()
        connectionJob?.cancel()
        updatesJob?.cancel()
        repository.cancel()
    }

}

/** Extension to convert Stock to StockDTO */
fun Stock.toDto(): StockDTO = StockDTO(id = this.id, symbolName = this.symbolName, price = this.price)
