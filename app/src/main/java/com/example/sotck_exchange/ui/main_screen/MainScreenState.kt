package com.example.stock_exchange.ui.main_screen

import com.example.sotck_exchange.domain.model.Stock

data class MainScreenState(
    val stocks: List<Stock> = mutableListOf(),
    val isConnected: Boolean = false,
    val isFeedRunning: Boolean = false,
) {
}