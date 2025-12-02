package com.biernatmdev.simple_service.core.ui.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun rememberOvershootScale(
    targetValue: Float = 0.7f,
    durationMillis: Int = 600,
    delayMillis: Int = 200,
    overshoot: Float = 7f
): Animatable<Float, *> {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = {
                    OvershootInterpolator(overshoot).getInterpolation(it)
                }
            )
        )
    }

    return scale
}

@Composable
fun rememberOvershootScales(
    count: Int,
    targetValue: Float = 0.7f,
    durationMillis: Int = 600,
    baseDelay: Int = 200,
    overshoot: Float = 7f
): List<Animatable<Float, *>> {
    return List(count) { index ->
        rememberOvershootScale(
            targetValue = targetValue,
            durationMillis = durationMillis,
            delayMillis = baseDelay * index,
            overshoot = overshoot
        )
    }
}
