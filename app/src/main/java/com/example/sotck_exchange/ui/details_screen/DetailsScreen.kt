package com.example.sotck_exchange.ui.details_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sotck_exchange.ui.components.FeedTopBar
import com.example.sotck_exchange.ui.components.FlashingPrice
import com.example.stock_exchange.ui.main_screen.MainScreenEvent

@SuppressLint("DefaultLocale")
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
) {


    val state = viewModel.state.value


    if (state.stock != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = state.stock.symbolName,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlashingPrice(
                    price = state.stock.price,
                    goingUp = state.stock.goingUp,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Description for ${state.stock.symbolName} goes here. You can fetch or hardcode symbol info.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
