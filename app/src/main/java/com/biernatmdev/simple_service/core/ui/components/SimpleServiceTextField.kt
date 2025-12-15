package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun SimpleServiceTextField(
    isPassword: Boolean = false,
    isEmail: Boolean = false,
    isPhoneNumber: Boolean = false,
    state: TextFieldState,
    placeholder: String = "",
    icon: IconType? = null,
    iconTint: Color = onColorBackgroundDarker,
    iconSize: Dp = 22.dp,
    backgroundColor: Color = ColorSecondary,
    textColor: Color = onColorBackgroundDarker,
    textSize: TextUnit = MEDIUM,
    roundedCornerShapeValue: Dp = 22.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    if (isPassword) {
        BasicSecureTextField(
            state = state,
            textStyle = TextStyle(
                color = onColorBackground,
                fontFamily = momoFont(),
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            interactionSource = interactionSource,
            textObfuscationMode = TextObfuscationMode.RevealLastTyped,
            cursorBrush = SolidColor(onColorBackground),
            decorator = { innerTextField ->
                SimpleServiceTextFieldDecorator(
                    innerTextField = innerTextField,
                    state = state,
                    isFocused = isFocused,
                    placeholder = placeholder,
                    icon = icon,
                    iconTint = iconTint,
                    iconSize = iconSize,
                    backgroundColor = backgroundColor,
                    textColor = textColor,
                    textSize = textSize,
                    roundedCornerShapeValue = roundedCornerShapeValue
                )
            }
        )
    } else {
        BasicTextField(
            state = state,
            textStyle = TextStyle(
                color = onColorBackground,
                fontFamily = momoFont(),
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
            ),
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                keyboardType = when {
                    isEmail -> KeyboardType.Email
                    isPhoneNumber -> KeyboardType.Phone
                    else -> KeyboardType.Text
                },
                imeAction = ImeAction.Done
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(onColorBackground),
            decorator = { innerTextField ->
                SimpleServiceTextFieldDecorator(
                    innerTextField = innerTextField,
                    state = state,
                    isFocused = isFocused,
                    placeholder = placeholder,
                    icon = icon,
                    iconTint = iconTint,
                    iconSize = iconSize,
                    backgroundColor = backgroundColor,
                    textColor = textColor,
                    textSize = textSize,
                    roundedCornerShapeValue = roundedCornerShapeValue
                )
            }
        )
    }
}

@Composable
fun SimpleServiceTextFieldDecorator(
    innerTextField: @Composable () -> Unit,
    state: TextFieldState,
    placeholder: String,
    isFocused: Boolean,
    icon: IconType?,
    iconTint: Color,
    iconSize: Dp,
    backgroundColor: Color,
    textColor: Color,
    textSize: TextUnit,
    roundedCornerShapeValue: Dp,
) {
    val modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(roundedCornerShapeValue))
        .background(backgroundColor)
        .padding(16.dp)
        .defaultMinSize(minHeight = iconSize)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val showPlaceholder = state.text.isEmpty() && !isFocused && placeholder.isNotEmpty()
        AnimatedVisibility(
            visible = showPlaceholder,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                if (icon != null) {
                    when (icon) {
                        is IconType.Drawable -> {
                            Icon(
                                painter = painterResource(id = icon.id),
                                tint = iconTint,
                                contentDescription = "$placeholder icon",
                                modifier = Modifier.size(iconSize),
                            )
                        }

                        is IconType.Vector -> {
                            Icon(
                                imageVector = icon.imageVector,
                                tint = iconTint,
                                contentDescription = "$placeholder icon",
                                modifier = Modifier.size(iconSize),
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                }
                Text(
                    text = placeholder,
                    fontFamily = momoFont(),
                    color = textColor,
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
        innerTextField()
    }
}