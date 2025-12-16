package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.onColorPrimary

@Composable
fun SimpleServiceButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ColorPrimary,
    icon: IconType? = null,
    isIconLeading: Boolean = false,
    isIconAtEdge: Boolean = false,
    iconSize: Dp = 32.dp,
    iconTint: Color = onColorPrimary,
    text: String,
    additionalText: String = "",
    loading: Boolean = false,
    textColor: Color = onColorPrimary,
    textFont: FontFamily = momoFont(),
    textFontSize: TextUnit = SEMI_LARGE,
    textFontWeight: FontWeight = FontWeight.Normal,
    spacerWidth: Dp = 16.dp,
    roundedCornerShapeValue: Dp = 22.dp,
    isAnimated: Boolean,
    onClick: () -> Unit,
) {
    var buttonText by remember { mutableStateOf(text) }
    if (isAnimated) {
        LaunchedEffect(loading) {
            buttonText = if (loading) additionalText else text
        }
    }

    val textContent = @Composable {
        Text(
            text = buttonText,
            color = textColor,
            fontFamily = textFont,
            fontSize = textFontSize,
            fontWeight = textFontWeight,
        )
    }

    val iconContent = @Composable {
        AnimatedContent(
            targetState = loading,
        ) { loadingState ->
            if (!loadingState) {
                if (icon != null) {
                    when (icon) {
                        is IconType.Vector -> {
                            Icon(
                                imageVector = icon.imageVector,
                                contentDescription = icon.imageVector.name,
                                tint = iconTint,
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        is IconType.Drawable -> {
                            Icon(
                                painter = painterResource(icon.id),
                                contentDescription = icon.id.toString(),
                                tint = iconTint,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize), strokeWidth = 3.dp, color = onColorPrimary
                )
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(roundedCornerShapeValue),
        color = backgroundColor,
    ) {
        if (isIconAtEdge && icon != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.align(
                        if (isIconLeading) Alignment.CenterStart else Alignment.CenterEnd
                    )
                ) { iconContent() }
                Box(
                    modifier = Modifier.align(Alignment.Center)
                ) { textContent() }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    if (isIconLeading) {
                        iconContent()
                        Spacer(Modifier.width(spacerWidth))
                        textContent()
                    } else {
                        textContent()
                        Spacer(Modifier.width(spacerWidth))
                        iconContent()
                    }
                } else {
                    textContent()
                }
            }
        }
    }
}

