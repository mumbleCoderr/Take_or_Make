package com.biernatmdev.simple_service.features.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.data.auth.GoogleUiClient
import com.biernatmdev.simple_service.features.components.Button
import com.biernatmdev.simple_service.features.components.IconType
import com.biernatmdev.simple_service.features.components.rememberOvershootScale
import com.biernatmdev.simple_service.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.ui.theme.ColorPrimaryText
import com.biernatmdev.simple_service.ui.theme.ColorSecondaryText
import com.biernatmdev.simple_service.ui.theme.ColorSurface
import com.biernatmdev.simple_service.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Handshake
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.LogIn
import com.biernatmdev.simple_service.ui.theme.momoFont
import com.google.firebase.auth.FirebaseAuth
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
            .background(ColorSurface)
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
            color = ColorPrimaryText,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(R.string.splash_subtext),
            color = ColorSecondaryText,
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














