package com.example.sotck_exchange.ui.components


import android.text.Layout
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay



@Composable
fun FlashingPrice(
    price: Float,
    goingUp: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    flashDuration: Long = 1000L, // flash for 1 second
    arrowFontSize: Float = 20f // make arrow bigger
) {
    var flashColor by remember { mutableStateOf(Color.Unspecified) }
    val animatedColor by animateColorAsState(targetValue = flashColor)

    LaunchedEffect(price, goingUp) {
        // Trigger flash color on price change
        flashColor = if (goingUp) Color(0xFF4CAF50) else Color(0xFFF44336)
        delay(flashDuration)
        flashColor = Color.Unspecified
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = String.format("%.2f", price),
            style = textStyle,
            color = if (flashColor != Color.Unspecified) animatedColor else textStyle.color
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = if (goingUp) "↑" else "↓",
            fontSize = arrowFontSize.sp,
            color = if (goingUp) Color(0xFF4CAF50) else Color(0xFFF44336)
        )
    }
}
