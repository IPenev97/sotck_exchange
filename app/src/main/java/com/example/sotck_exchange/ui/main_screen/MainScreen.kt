package com.example.sotck_exchange.ui.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sotck_exchange.ui.components.FeedTopBar
import com.example.sotck_exchange.ui.components.StockRow
import com.example.stock_exchange.ui.main_screen.MainScreenEvent
import com.example.stock_exchange.ui.main_screen.MainViewModel


@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel(), onStockClicked: (Long, Boolean)-> Unit) {
     val state = viewModel.state.value

     LaunchedEffect(Unit) {
          viewModel.syncFeedState()
     }


     Column(modifier = Modifier.fillMaxSize()) {
          FeedTopBar(
               isConnected = state.isConnected,
               isFeedRunning = state.isFeedRunning,
               onToggleFeed = { viewModel.onEvent(MainScreenEvent.ToggleFeed) }
          )


          LazyColumn(
               modifier = Modifier.fillMaxSize(),
               contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
               items(state.stocks) { stock ->
                    StockRow(stock, onClick = {onStockClicked(stock.id, state.isFeedRunning)})
               }
          }
     }
}