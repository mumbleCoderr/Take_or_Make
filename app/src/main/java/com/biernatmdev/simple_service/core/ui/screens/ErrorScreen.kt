package com.biernatmdev.simple_service.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ErrorFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.RetryOutlined
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground

@Composable
fun ErrorScreen(
    error: UiText,
    isLoading: Boolean,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(y = ((-60).dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ErrorFilled,
                modifier = Modifier.size(222.dp),
                contentDescription = ErrorFilled.name,
                tint = ColorPrimary
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = error.asString(),
                color = onColorBackground,
                fontFamily = momoFont(),
                fontSize = EXTRA_MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.EXTRA_MEDIUM,
                textAlign = TextAlign.Center
            )
        }
        SimpleServiceButton(
            onClick = { onRetry() },
            isLoading = isLoading,
            icon = IconType.Vector(RetryOutlined),
            text = stringResource(R.string.error_btn_retry),
            isAnimated = true,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = ((-30).dp))
        )
    }
}