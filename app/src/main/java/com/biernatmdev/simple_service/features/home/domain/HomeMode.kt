package com.biernatmdev.simple_service.features.home.domain

import androidx.annotation.StringRes
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.features.components.IconType
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Make
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Take

enum class HomeMode(
    val icon: IconType,
    @StringRes val title: Int
) {
    TAKE(
        icon = IconType.Drawable(Take),
        title = R.string.top_bar_take
    ),
    MAKE(
        icon = IconType.Drawable(Make),
        title = R.string.top_bar_make
    )
}