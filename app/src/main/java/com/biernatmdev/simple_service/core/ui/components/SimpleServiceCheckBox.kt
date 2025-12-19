package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SMALL
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground

@Composable
fun SimpleServiceCheckBox(
    onClick: () -> Unit,
    text: AnnotatedString,
    errorText: UiText?,
    isChecked: Boolean,
) {
    val isError = errorText != null

    Column(
            modifier = Modifier
                .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = if (isError) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                    checkedColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                )
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = text,
                fontFamily = momoFont(),
                fontSize = REGULAR,
                lineHeight = LineHeight.REGULAR,
                color = onColorBackground,
            )
        }
        AnimatedVisibility(
            visible = isError,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = errorText?.asString() ?: "",
                color = Color.Red,
                style = TextStyle(
                    fontFamily = momoFont(),
                    fontSize = SMALL,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}