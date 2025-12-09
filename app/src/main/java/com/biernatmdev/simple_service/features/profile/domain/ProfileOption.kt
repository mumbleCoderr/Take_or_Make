package com.biernatmdev.simple_service.features.profile.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Details
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Favourites
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.History
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Pro
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Reviews
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Statistics
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Wallet

enum class ProfileOption(
    val icon: IconType,
    val title: UiText,
    val category: ProfileOptionCategory,
    val screen: Screen,
) {
    DETAILS(
        icon = IconType.Vector(Details),
        title = UiText.StringResource(R.string.profile_option_details),
        category = ProfileOptionCategory.GENERAL,
        screen = Screen.DetailsScreen
    ),
    WALLET(
        icon = IconType.Vector(Wallet),
        title = UiText.StringResource(R.string.profile_option_wallet),
        category = ProfileOptionCategory.GENERAL,
        screen = Screen.WalletScreen
    ),
    PRO(
        icon = IconType.Vector(Pro),
        title = UiText.StringResource(R.string.profile_option_pro),
        category = ProfileOptionCategory.GENERAL,
        screen = Screen.ProScreen
    ),
    STATISTICS(
        icon = IconType.Drawable(Statistics),
        title = UiText.StringResource(R.string.profile_option_statistics),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.StatisticsScreen
    ),
    HISTORY(
        icon = IconType.Vector(History),
        title = UiText.StringResource(R.string.profile_option_history),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.HistoryScreen
    ),
    REVIEWS(
        icon = IconType.Vector(Reviews),
        title = UiText.StringResource(R.string.profile_option_reviews),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.ReviewsScreen
    ),
    FAVOURITES(
        icon = IconType.Vector(Favourites),
        title = UiText.StringResource(R.string.profile_option_favourites),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.FavouritesScreen
    ),
}

