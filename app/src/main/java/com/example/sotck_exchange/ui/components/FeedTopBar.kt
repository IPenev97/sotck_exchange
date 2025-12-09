package com.example.sotck_exchange.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(
    isConnected: Boolean,
    isFeedRunning: Boolean,
    onToggleFeed: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Connection Status
                val statusColor = if (isConnected) Color(0xFF4CAF50) else Color(0xFFF44336)
                Text(
                    text = if (isConnected) "🟢 Connected" else "🔴 Disconnected",
                    color = statusColor,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Start/Stop Toggle Button
                Button(
                    onClick = onToggleFeed,
                    enabled = isConnected // disable if not connected
                ) {
                    Text(
                        text = if (isConnected) {
                            if (isFeedRunning) "Stop Feed" else "Start Feed"
                        } else {
                            "Start Feed" // always show Start Feed when disconnected
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
