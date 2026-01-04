package com.biernatmdev.simple_service.core.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.biernatmdev.simple_service.features.auth.presentation.AuthScreen
import com.biernatmdev.simple_service.features.user_details.presentation.UserDetailsScreen
import com.biernatmdev.simple_service.features.favourites.FavouritesScreen
import com.biernatmdev.simple_service.features.history.HistoryScreen
import com.biernatmdev.simple_service.features.home.presentation.HomeScreen
import com.biernatmdev.simple_service.features.pro.ProScreen
import com.biernatmdev.simple_service.features.reviews.ReviewsScreen
import com.biernatmdev.simple_service.features.statistics.StatistiscScreen
import com.biernatmdev.simple_service.features.splash.SplashScreen
import com.biernatmdev.simple_service.features.wallet.WalletScreen

@Composable
fun SimpleServiceNavGraph(startDestination: Screen = Screen.SplashScreen) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.SplashScreen> {
            SplashScreen(
                navigateToAuth = {
                    navController.navigate(Screen.AuthScreen) {
                    }
                }
            )
        }
        composable<Screen.AuthScreen> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeScreen(
                navigateToAuth = {
                    navController.navigate(Screen.AuthScreen) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToProfileSubscreen = { screen ->
                    navController.navigate(screen)
                }
            )
        }
        composable<Screen.UserDetailsScreen> {
            UserDetailsScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.WalletScreen> {
            WalletScreen()
        }
        composable<Screen.ProScreen> {
            ProScreen()
        }
        composable<Screen.StatisticsScreen> {
            StatistiscScreen()
        }
        composable<Screen.HistoryScreen> {
            HistoryScreen()
        }
        composable<Screen.ReviewsScreen> {
            ReviewsScreen()
        }
        composable<Screen.FavouritesScreen> {
            FavouritesScreen()
        }
    }
}