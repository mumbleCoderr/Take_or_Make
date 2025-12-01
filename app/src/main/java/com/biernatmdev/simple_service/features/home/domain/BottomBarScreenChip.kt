package com.biernatmdev.simple_service.features.home.domain

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.features.nav.Screen
import com.biernatmdev.simple_service.ui.theme.Resources

enum class BottomBarScreenChip(
    val icon: ImageVector,
    @StringRes val title: Int,
    val screen: Screen,
) {
    HOME_SCREEN(
        icon = Resources.Icon.Home,
        title = R.string.bottom_bar_home,
        screen = Screen.HomeScreen,
    ),
    CATEGORY(
        icon = Resources.Icon.Category,
        title = R.string.bottom_bar_category,
        screen = Screen.CategoryScreen,
    ),
    NOTIFICATIONS(
        icon = Resources.Icon.Notification,
        title = R.string.bottom_bar_notifications,
        screen = Screen.NotificationScreen,
    ),
    PROFILE(
        icon = Resources.Icon.Profile,
        title = R.string.bottom_bar_profile,
        screen = Screen.ProfileScreen,
    )
}