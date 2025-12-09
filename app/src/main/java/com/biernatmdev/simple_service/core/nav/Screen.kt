package com.biernatmdev.simple_service.core.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class
Screen {

    // MAIN SCREENS
    @Serializable
    data object SplashScreen: Screen()
    @Serializable
    data object AuthScreen: Screen()
    @Serializable
    data object HomeGraph: Screen()

    // HOME GRAPH SUBSCREENS
    @Serializable
    data object HomeScreen: Screen()
    @Serializable
    data object CategoryScreen: Screen()
    @Serializable
    data object NotificationScreen: Screen()
    @Serializable
    data object ProfileScreen: Screen()

    // PROFILE SUBSCREENS
    @Serializable
    data object DetailsScreen: Screen()
    @Serializable
    data object WalletScreen: Screen()
    @Serializable
    data object ProScreen: Screen()
    @Serializable
    data object StatisticsScreen: Screen()
    @Serializable
    data object HistoryScreen: Screen()
    @Serializable
    data object ReviewsScreen: Screen()
    @Serializable
    data object FavouritesScreen: Screen()
}