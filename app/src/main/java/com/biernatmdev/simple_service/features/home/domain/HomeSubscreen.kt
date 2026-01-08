package com.biernatmdev.simple_service.features.home.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.AccountFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.AccountOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.FavouriteFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.FavouriteOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.HomeFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.HomeOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.NotificationFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.NotificationOutlined

enum class HomeSubscreen(
    val iconFocused: IconType,
    val iconNotFocused: IconType,
    val title: UiText,
    val screen: Screen,
) {
    HOME(
        iconFocused = IconType.Vector(HomeFilled),
        iconNotFocused = IconType.Vector(HomeOutlined),
        title = UiText.StringResource(R.string.home_bottom_nav_bar_home),
        screen = Screen.HomeScreen,
    ),
    FAVOURITES(
        iconFocused = IconType.Vector(FavouriteFilled),
        iconNotFocused = IconType.Vector(FavouriteOutlined),
        title = UiText.StringResource(R.string.home_bottom_nav_bar_favourites),
        screen = Screen.FavouritesScreen,
    ),
    NOTIFICATIONS(
        iconFocused = IconType.Vector(NotificationFilled),
        iconNotFocused = IconType.Vector(NotificationOutlined),
        title = UiText.StringResource(R.string.home_bottom_nav_bar_notifications),
        screen = Screen.NotificationScreen,
    ),
    PROFILE(
        iconFocused = IconType.Vector(AccountFilled),
        iconNotFocused = IconType.Vector(AccountOutlined),
        title = UiText.StringResource(R.string.home_bottom_nav_bar_profile),
        screen = Screen.ProfileScreen,
    )
}