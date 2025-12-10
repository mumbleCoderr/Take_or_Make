package com.biernatmdev.simple_service.features.main

import com.biernatmdev.simple_service.core.nav.Screen

data class MainState(
    val isLoading: Boolean = true,
    val startDestination: Screen = Screen.SplashScreen
)