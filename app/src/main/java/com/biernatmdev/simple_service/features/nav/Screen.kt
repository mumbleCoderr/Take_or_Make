package com.biernatmdev.simple_service.features.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object SplashScreen: Screen()

    @Serializable
    data object AuthScreen: Screen()

    @Serializable
    data object HomeGraph: Screen()

}