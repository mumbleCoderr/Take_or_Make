package com.biernatmdev.simple_service.features.home.domain

import androidx.annotation.StringRes
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.features.components.IconType
import com.biernatmdev.simple_service.features.nav.Screen
import com.biernatmdev.simple_service.ui.theme.Resources

enum class BottomBarScreen(
    val icon: IconType,
    @StringRes val title: Int,
    val screen: Screen,
) {
    HOME(
        icon = IconType.Vector(Resources.Icon.Home),
        title = R.string.bottom_bar_home,
        screen = Screen.HomeScreen,
    ),
    CATEGORY(
        icon = IconType.Vector(Resources.Icon.Category),
        title = R.string.bottom_bar_category,
        screen = Screen.CategoryScreen,
    ),
    NOTIFICATIONS(
        icon = IconType.Vector(Resources.Icon.Notification),
        title = R.string.bottom_bar_notifications,
        screen = Screen.NotificationScreen,
    ),
    PROFILE(
        icon = IconType.Drawable(Resources.Icon.Profile),
        title = R.string.bottom_bar_profile,
        screen = Screen.ProfileScreen,
    )
}