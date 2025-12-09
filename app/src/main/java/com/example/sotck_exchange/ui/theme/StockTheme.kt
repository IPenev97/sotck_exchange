import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.material3.Shapes
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.sotck_exchange.ui.theme.Themes

@Composable
fun StockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // auto-detect system theme
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) Themes.DarkColors else Themes.LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(), // optionally define your own
        shapes = Shapes(), // optionally define your own
        content = content
    )
}
