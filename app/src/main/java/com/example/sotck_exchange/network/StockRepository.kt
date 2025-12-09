package com.example.sotck_exchange.network

import android.util.Log
import com.example.sotck_exchange.domain.IStockRepository
import com.example.sotck_exchange.domain.dto.StockDTO
import com.example.stock_exchange.network.Resource
import com.example.stock_exchange.network.SocketCommunicator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val socketCommunicator: SocketCommunicator
) : IStockRepository {

    private val TAG = "StockRepository"

    private val wsScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var sendingJob: Job? = null
    private val reconnectDelayMs = 3000L
    private var isFeedRunning = false
    private var host = "wss://ws.postman-echo.com/raw"

    private val stocksList: MutableList<StockDTO> = mutableListOf(
        StockDTO(1, "AAPL", 0f),
        StockDTO(2, "AMZN", 0f),
        StockDTO(3, "MSFT", 0f),
        StockDTO(4, "GOOGL", 0f),
        StockDTO(5, "META", 0f),
        StockDTO(6, "TSLA", 0f),
        StockDTO(7, "NVDA", 0f),
        StockDTO(8, "NFLX", 0f),
        StockDTO(9, "ORCL", 0f),
        StockDTO(10, "IBM", 0f),
        StockDTO(11, "INTC", 0f),
        StockDTO(12, "AMD", 0f),
        StockDTO(13, "ADBE", 0f),
        StockDTO(14, "PYPL", 0f),
        StockDTO(15, "UBER", 0f),
        StockDTO(16, "LYFT", 0f),
        StockDTO(17, "SHOP", 0f),
        StockDTO(18, "SAP", 0f),
        StockDTO(19, "BABA", 0f),
        StockDTO(20, "SONY", 0f),
        StockDTO(21, "V", 0f),
        StockDTO(22, "MA", 0f),
        StockDTO(23, "DIS", 0f),
        StockDTO(24, "KO", 0f),
        StockDTO(25, "PEP", 0f)
    )

    private val _connectionState = MutableStateFlow(false)
    override val connectionState: StateFlow<Boolean> get() = _connectionState

    override val stockUpdates: MutableSharedFlow<Resource<StockDTO>> = MutableSharedFlow()

    /** Fetch all stocks */
    override fun getAllStocks(): List<StockDTO> = stocksList.toList()

    /** Fetch stock by ID */
    override fun getStockById(id: Long): StockDTO? = stocksList.find { it.id == id }

    override fun startFeed() {
        isFeedRunning = true
        startSendingUpdates()
    }

    override fun stopFeed() {
        isFeedRunning = false
        stopSendingUpdates()
    }

    private fun startSendingUpdates() {
        if (!isFeedRunning || !_connectionState.value) return
        if (sendingJob?.isActive == true) return

        sendingJob = wsScope.launch {
            while (isActive && isFeedRunning && _connectionState.value) {
                stocksList.forEach { stock ->
                    val update = StockDTO(
                        id = stock.id,
                        symbolName = stock.symbolName,
                        price = ((100..200).random() + Math.random()).toFloat()
                    )
                    socketCommunicator.send(update)
                }
                delay(2000)
            }
        }
    }

    private fun stopSendingUpdates() {
        sendingJob?.cancel()
        sendingJob = null
    }

    /** Connect to the WebSocket if not already connected */
    override fun connect() {
        // If already connected, do nothing
        if (_connectionState.value) {
            Log.d(TAG, "WebSocket already connected, skipping connect()")
            return
        }

        wsScope.launch {
            while (isActive) {
                try {
                    socketCommunicator.connectAndListen(host).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _connectionState.value = true
                                startSendingUpdates()
                            }
                            is Resource.Success -> {
                                val updatedStock = result.data
                                val index = stocksList.indexOfFirst { it.id == updatedStock.id }
                                if (index != -1) {
                                    stocksList[index] = updatedStock
                                }
                                stockUpdates.emit(result)
                            }
                            is Resource.Error -> {
                                _connectionState.value = false
                                stopSendingUpdates()
                                Log.e(TAG, "WebSocket error", result.throwable)
                                throw result.throwable ?: Exception("Unknown WS error")
                            }
                        }
                    }
                } catch (e: Exception) {
                    _connectionState.value = false
                    Log.d(TAG, "Reconnect attempt in $reconnectDelayMs ms")
                    delay(reconnectDelayMs)
                }
            }
        }
    }

    override fun cancel() {
        wsScope.cancel()
    }
}
