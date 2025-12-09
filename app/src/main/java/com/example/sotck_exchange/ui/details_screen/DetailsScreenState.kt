package com.example.sotck_exchange.ui.details_screen

import com.example.sotck_exchange.domain.model.Stock

data class DetailsScreenState(
    val stock: Stock? = null,
    val isConnected: Boolean = false,
    val isFeedRunning: Boolean = false,
) {
}