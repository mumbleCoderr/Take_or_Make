package com.biernatmdev.simple_service.core.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.biernatmdev.simple_service.features.auth.presentation.AuthScreen
import com.biernatmdev.simple_service.features.home.presentation.HomeScreen
import com.biernatmdev.simple_service.features.profile.presentation.ProfileScreen
import com.biernatmdev.simple_service.features.splash.SplashScreen

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
                        popUpTo<Screen.SplashScreen> { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.AuthScreen> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.AuthScreen> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.AuthScreen> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeScreen(
                navigateToAuth = {
                    navController.navigate(Screen.AuthScreen) {
                        popUpTo<Screen.AuthScreen> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.ProfileScreen> {
            ProfileScreen(
                navigateToAuth = {
                    navController.navigate(Screen.AuthScreen) {
                        popUpTo<Screen.AuthScreen> { inclusive = true }
                    }
                }
            )
        }
    }
}