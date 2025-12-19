package com.biernatmdev.simple_service.features.profile.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.DetailsFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.FavouritesFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.HistoryFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LinkFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.SignInFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ProFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ReviewsFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.StatisticsFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.WalletFilled

enum class ProfileOption(
    val icon: IconType,
    val title: UiText,
    val category: ProfileOptionCategory,
    val screen: Screen,
) {
    USER_DETAILS(
        icon = IconType.Vector(DetailsFilled),
        title = UiText.StringResource(R.string.profile_option_details),
        category = ProfileOptionCategory.GENERAL,
        screen = Screen.UserDetailsScreen
    ),
    WALLET(
        icon = IconType.Vector(WalletFilled),
        title = UiText.StringResource(R.string.profile_option_wallet),
        category = ProfileOptionCategory.GENERAL,
        screen = Screen.WalletScreen
    ),
    PRO(
        icon = IconType.Vector(ProFilled),
        title = UiText.StringResource(R.string.profile_option_pro),
        category = ProfileOptionCategory.GENERAL,
        screen = Screen.ProScreen
    ),
    STATISTICS(
        icon = IconType.Drawable(StatisticsFilled),
        title = UiText.StringResource(R.string.profile_option_statistics),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.StatisticsScreen
    ),
    HISTORY(
        icon = IconType.Vector(HistoryFilled),
        title = UiText.StringResource(R.string.profile_option_history),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.HistoryScreen
    ),
    REVIEWS(
        icon = IconType.Vector(ReviewsFilled),
        title = UiText.StringResource(R.string.profile_option_reviews),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.ReviewsScreen
    ),
    FAVOURITES(
        icon = IconType.Vector(FavouritesFilled),
        title = UiText.StringResource(R.string.profile_option_favourites),
        category = ProfileOptionCategory.ACTIVITY,
        screen = Screen.FavouritesScreen
    ),
    LINK_ACCOUNT(
        icon = IconType.Vector(LinkFilled),
        title = UiText.StringResource(R.string.profile_option_link_account),
        category = ProfileOptionCategory.OTHER,
        screen = Screen.UserDetailsScreen
    ),
    SIGNOUT(
        icon = IconType.Vector(SignInFilled),
        title = UiText.StringResource(R.string.profile_option_logout),
        category = ProfileOptionCategory.OTHER,
        screen = Screen.SplashScreen
    )
}

