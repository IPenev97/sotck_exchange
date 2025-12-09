package com.example.sotck_exchange.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.sotck_exchange.ui.details_screen.DetailsScreen
import com.example.sotck_exchange.ui.main_screen.MainScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "feed"
    ) {
        composable("feed") {
            MainScreen(
                onStockClicked = { stockId, isFeedRunning ->
                    navController.navigate("details/$stockId?feedRunning=$isFeedRunning")
                }
            )
        }

        composable(
            route = "details/{stockId}",
            deepLinks = listOf(navDeepLink { uriPattern = "stocks://symbol/{stockId}" }),
                    arguments = listOf(
                navArgument("stockId") { type = NavType.LongType },
            )
        ) {
            DetailsScreen()
        }
    }
}
