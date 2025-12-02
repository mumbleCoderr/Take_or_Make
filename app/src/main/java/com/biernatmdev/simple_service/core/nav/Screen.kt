package com.biernatmdev.simple_service.core.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object SplashScreen: Screen()
    @Serializable
    data object AuthScreen: Screen()
    @Serializable
    data object HomeGraph: Screen()
    @Serializable
    data object HomeScreen: Screen()
    @Serializable
    data object CategoryScreen: Screen()
    @Serializable
    data object NotificationScreen: Screen()
    @Serializable
    data object ProfileScreen: Screen()
}