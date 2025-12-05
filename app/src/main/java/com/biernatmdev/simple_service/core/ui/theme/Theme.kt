package com.biernatmdev.simple_service.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = DarkModeBackgroundColor,

    background = DarkModeBackgroundColor,
    onBackground = DarkModePrimaryTextColor,

    surface = DarkModeBackgroundColor,
    onSurface = DarkModePrimaryTextColor,

    secondary = DarkModeSecondaryColor,
    onSecondary = DarkModeSecondaryTextColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = LightModePrimaryTextColor,

    background = LightModeBackgroundColor,
    onBackground = LightModePrimaryTextColor,

    surface = LightModeBackgroundColor,
    onSurface = LightModePrimaryTextColor,

    secondary = LightModeSecondaryColor,
    onSecondary = LightModeSecondaryTextColor
)

val ColorPrimary: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.primary

val onColorPrimary: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.onPrimary

val ColorBackground: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.background

val onColorBackground: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.onBackground

val ColorSurface: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.surface

val onColorSurface: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.onSurface

val ColorSecondary: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.secondary

val onColorBackgroundDarker: androidx.compose.ui.graphics.Color
    @Composable
    get() = MaterialTheme.colorScheme.onSecondary

@Composable
fun SimpleServiceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}