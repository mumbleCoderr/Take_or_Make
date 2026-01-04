package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.momoFont

@Composable
fun SimpleServiceDivider(
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (text.isBlank()) {
            HorizontalDivider(
                color = color, thickness = 1.dp
            )
        } else {
            HorizontalDivider(
                modifier = Modifier.weight(1f), color = color, thickness = 1.dp
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = text,
                color = color,
                fontFamily = momoFont(),
                fontSize = REGULAR,
                fontWeight = FontWeight.Normal,
            )
            Spacer(Modifier.width(16.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f), color = color, thickness = 1.dp
            )
        }
    }
}