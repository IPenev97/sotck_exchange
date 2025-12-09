package com.example.sotck_exchange.domain.dto

import com.example.sotck_exchange.domain.model.Stock

data class StockDTO(
    val id: Long,
    val symbolName: String,
    val price: Float
) {

}
fun StockDTO.toModel(): Stock = Stock(
    id = this.id,
    symbolName = this.symbolName,
    price = this.price,
    goingUp = false
)
