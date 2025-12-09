package com.example.sotck_exchange.domain.model

data class Stock(
    val id: Long,
    val price: Float,
    val symbolName: String,
    val goingUp: Boolean,
) {
}