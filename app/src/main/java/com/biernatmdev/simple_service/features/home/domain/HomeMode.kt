package com.biernatmdev.simple_service.features.home.domain

import androidx.annotation.StringRes
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.theme.Resources

enum class HomeMode(
    val icon: IconType,
    @StringRes val title: Int
) {
    TAKE(
        icon = IconType.Drawable(Resources.Icon.Take),
        title = R.string.home_top_nav_bar_take
    ),
    MAKE(
        icon = IconType.Drawable(Resources.Icon.Make),
        title = R.string.home_top_nav_bar_make
    )
}