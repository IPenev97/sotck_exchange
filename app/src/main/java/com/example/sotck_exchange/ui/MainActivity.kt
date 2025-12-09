package com.example.sotck_exchange.ui

import StockTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sotck_exchange.domain.model.Stock
import com.example.sotck_exchange.ui.details_screen.DetailsScreen
import com.example.sotck_exchange.ui.details_screen.DetailsViewModel
import com.example.sotck_exchange.ui.main_screen.MainScreen
import com.example.stock_exchange.ui.main_screen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavHost(navController)


                }
            }
        }
    }
}
