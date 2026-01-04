package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun SimpleServiceDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    backgroundColor: Color = ColorBackground,
    title: UiText,
    titleColor: Color = onColorBackground,
    titleFontSize: TextUnit = SEMI_LARGE,
    subtext: UiText,
    subtextColor: Color = onColorBackgroundDarker,
    subtextFontSize: TextUnit = MEDIUM,
    subtextLineHeight: TextUnit = LineHeight.MEDIUM,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = title.asString(),
                fontSize = titleFontSize,
                color = titleColor,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = subtext.asString(),
                fontSize = subtextFontSize,
                color = subtextColor,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Normal,
                lineHeight = subtextLineHeight,
                modifier = Modifier.padding(bottom = 22.dp),
            )
        },
        confirmButton = {
            SimpleServiceButton(
                modifier = Modifier,
                buttonPadding = 8.dp,
                roundedCornerShapeValue = 16.dp,
                text = stringResource(R.string.dialog_ok),
                textFontSize = MEDIUM,
                isAnimated = false,
                onClick = { onConfirm() }
            )
        },
        dismissButton = {
            SimpleServiceButton(
                modifier = Modifier,
                backgroundColor = ColorSecondary,
                isBordered = true,
                buttonPadding = 8.dp,
                roundedCornerShapeValue = 16.dp,
                text = stringResource(R.string.dialog_dismiss),
                textColor = ColorPrimary,
                textFontSize = MEDIUM,
                isAnimated = false,
                onClick = { onDismiss() }
            )
        },
        containerColor = backgroundColor
    )
}