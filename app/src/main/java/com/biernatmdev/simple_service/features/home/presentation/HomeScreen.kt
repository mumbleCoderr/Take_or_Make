package com.biernatmdev.simple_service.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.biernatmdev.simple_service.features.home.components.BottomBar
import com.biernatmdev.simple_service.features.home.domain.HomeSubscreen
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.features.profile.presentation.ProfileScreen

@Composable
fun HomeScreen(
    navigateToAuth: () -> Unit
) {
    // BOTTOM BAR NAVIGATION
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedScreenChip = HomeSubscreen.entries.find { chip ->
        currentDestination?.hierarchy?.any { it.hasRoute(chip.screen::class) } == true
    } ?: HomeSubscreen.HOME

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedScreen = selectedScreenChip,
                onScreenSelect = { selection ->
                    navController.navigate(selection.screen) {
                        launchSingleTop = true
                        popUpTo<Screen.HomeScreen> {
                            saveState = true
                            inclusive = false
                        }
                        restoreState = true
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorBackground)
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = Screen.HomeScreen
            ) {
                composable<Screen.HomeScreen> {
                    HomeTabScreen()
                }
                composable<Screen.CategoryScreen> {}
                composable<Screen.NotificationScreen> {}
                composable<Screen.ProfileScreen> {
                    ProfileScreen(navigateToAuth = navigateToAuth)
                }
            }
        }
    }
}