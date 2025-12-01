package com.biernatmdev.simple_service.features.components

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
sealed interface IconType {
    data class Vector(val imageVector: ImageVector) : IconType
    data class Drawable(@DrawableRes val id: Int) : IconType
}