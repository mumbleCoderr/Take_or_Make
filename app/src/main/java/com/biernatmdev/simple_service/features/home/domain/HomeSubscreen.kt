package com.biernatmdev.simple_service.features.home.domain

import androidx.annotation.StringRes
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.theme.Resources

enum class HomeSubscreen(
    val icon: IconType,
    @StringRes val title: Int,
    val screen: Screen,
) {
    HOME(
        icon = IconType.Vector(Resources.Icon.HomeFilled),
        title = R.string.home_bottom_nav_bar_home,
        screen = Screen.HomeScreen,
    ),
    CATEGORY(
        icon = IconType.Vector(Resources.Icon.CategoryFilled),
        title = R.string.home_bottom_nav_bar_category,
        screen = Screen.CategoryScreen,
    ),
    NOTIFICATIONS(
        icon = IconType.Vector(Resources.Icon.NotificationFilled),
        title = R.string.home_bottom_nav_bar_notifications,
        screen = Screen.NotificationScreen,
    ),
    PROFILE(
        icon = IconType.Drawable(Resources.Icon.ProfileFilled),
        title = R.string.home_bottom_nav_bar_profile,
        screen = Screen.ProfileScreen,
    )
}