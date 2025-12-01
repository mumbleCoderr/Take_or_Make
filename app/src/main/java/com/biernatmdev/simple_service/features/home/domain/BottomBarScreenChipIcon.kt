package com.biernatmdev.simple_service.features.home.domain

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface BottomBarScreenChipIcon {
    data class Vector(val imageVector: ImageVector) : BottomBarScreenChipIcon
    data class Drawable(@DrawableRes val id: Int) : BottomBarScreenChipIcon
}