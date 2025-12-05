package com.biernatmdev.simple_service.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.ui.components.Button
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.components.rememberOvershootScale
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Handshake
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LogIn
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import org.koin.compose.koinInject

@Composable
//@Preview(showBackground = true)
fun SplashScreen(
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val googleUiClient: GoogleUiClient = koinInject()

    val scale = rememberOvershootScale()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Handshake,
            contentDescription = "Handshake",
            modifier = Modifier
                .scale(scale.value)
                .size(220.dp),
            tint = ColorPrimary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.splash_header),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(R.string.splash_subtext),
            color = onColorBackgroundDarker,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 1.2 * LARGE
        )
        Spacer(Modifier.height(100.dp))
        Button(
            onClick = {
                val user = googleUiClient.currentUser
                if (user != null) {
                    navigateToHome()
                } else {
                    navigateToAuth()
                }
            },
            icon = IconType.Vector(LogIn),
            text = stringResource(R.string.splash_btn_text),
            isAnimated = false
        )
    }
}














