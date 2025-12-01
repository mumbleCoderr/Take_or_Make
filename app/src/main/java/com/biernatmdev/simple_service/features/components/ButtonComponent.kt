package com.biernatmdev.simple_service.features.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.ui.theme.ColorBtnText
import com.biernatmdev.simple_service.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.ui.theme.ColorPrimaryText
import com.biernatmdev.simple_service.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.ui.theme.momoFont

@Composable
fun Button(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ColorPrimary,
    icon: IconType? = null,
    iconSize: Dp = 32.dp,
    iconTint: Color = ColorBtnText,
    text: String,
    additionalText: String = "",
    loading: Boolean = false,
    textColor: Color = ColorBtnText,
    textFont: FontFamily = momoFont(),
    textFontSize: TextUnit = SEMI_LARGE,
    textFontWeight: FontWeight = FontWeight.Normal,
    spacerWidth: Dp = 0.dp, //TODO FIX/CHANGE
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(roundedCornerShapeValue),
        color = backgroundColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 32.dp, 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                color = textColor,
                fontFamily = textFont,
                fontSize = textFontSize,
                fontWeight = textFontWeight,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.width(spacerWidth))
            AnimatedContent(
                targetState = loading,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) { loadingState ->
                if (!loadingState) {
                    if (icon != null) {
                        when (icon) {
                            is IconType.Vector -> {
                                Icon(
                                    imageVector = icon.imageVector,
                                    contentDescription = icon.imageVector.name,
                                    tint = iconTint,
                                    modifier = Modifier
                                        .size(iconSize)
                                )
                            }

                            is IconType.Drawable -> {
                                Icon(
                                    painter = painterResource(icon.id),
                                    contentDescription = icon.id.toString(),
                                    tint = iconTint,
                                    modifier = Modifier
                                        .size(iconSize)
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(iconSize)
                            .align(Alignment.Center),
                        strokeWidth = 3.dp,
                        color = ColorBtnText
                    )
                }
            }
        }
    }
}