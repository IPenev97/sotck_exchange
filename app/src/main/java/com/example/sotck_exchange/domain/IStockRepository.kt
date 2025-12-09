package com.example.sotck_exchange.domain

import com.example.sotck_exchange.domain.dto.StockDTO
import com.example.stock_exchange.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IStockRepository {

    val connectionState: StateFlow<Boolean>
    val stockUpdates: Flow<Resource<StockDTO>>

    fun startFeed()
    fun stopFeed()
    fun connect()
    fun cancel()

    fun getAllStocks(): List<StockDTO>
    fun getStockById(id: Long): StockDTO?
}
