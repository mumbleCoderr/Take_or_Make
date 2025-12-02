package com.biernatmdev.simple_service.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import com.biernatmdev.simple_service.features.home.domain.BottomBarScreen
import com.biernatmdev.simple_service.features.nav.Screen
import com.biernatmdev.simple_service.ui.theme.ColorSurface

@Composable
fun HomeScreen() {
    // BOTTOM BAR NAVIGATION
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedScreenChip = BottomBarScreen.entries.find { chip ->
        currentDestination?.hierarchy?.any { it.hasRoute(chip.screen::class) } == true
    } ?: BottomBarScreen.HOME

    Scaffold(
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
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
                .background(ColorSurface)
                .padding(padding)
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
                composable<Screen.ProfileScreen> {}
            }
        }
    }
}