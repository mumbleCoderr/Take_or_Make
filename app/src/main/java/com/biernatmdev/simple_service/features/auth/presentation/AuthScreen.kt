package com.biernatmdev.simple_service.features.auth.presentation

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.ui.components.Button
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.components.rememberOvershootScales
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Approval
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Campaign
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Construction
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Google
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LogIn
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Sell
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.core.ui.theme.onColorPrimary
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

//@Preview(showBackground = true)
@Composable
fun AuthScreen(
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    // KOIN INJECTION
    val authViewModel: AuthViewModel = koinViewModel()
    val googleUiClient: GoogleUiClient = koinInject()

    val state by authViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        authViewModel.effect.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToHome -> navigateToHome()
            }
        }
    }

    // TODO TOAST ONLY IN CASE OF EXTERNAL ERROR
    /*LaunchedEffect(state.error) {
        state.error?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            authViewModel.onAuthEvent(AuthEvent.ClearError)
        }
    }*/

    val iconSize = 180.dp
    val scales = rememberOvershootScales(count = 4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Construction,
                contentDescription = "Construction",
                modifier = Modifier
                    .scale(scales[0].value)
                    .size(iconSize),
                tint = ColorPrimary
            )
            Icon(
                imageVector = Sell,
                contentDescription = "Sell",
                modifier = Modifier
                    .scale(scales[1].value)
                    .size(iconSize),
                tint = ColorPrimary
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Campaign,
                contentDescription = "Campaign",
                modifier = Modifier
                    .scale(scales[2].value)
                    .size(iconSize),
                tint = ColorPrimary
            )
            Icon(
                painter = painterResource(Approval),
                contentDescription = "Approval",
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .scale(scales[3].value)
                    .size(iconSize),
                tint = ColorPrimary
            )
        }
        Spacer(Modifier.height(48.dp))
        AnimatedContent(
            targetState = state.isLoading
        ) { isLoading ->
            if (!isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.auth_header),
                        color = onColorBackground,
                        fontFamily = momoFont(),
                        fontSize = LARGE,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(22.dp))
                    Text(
                        text = stringResource(R.string.auth_subtext),
                        color = onColorBackgroundDarker,
                        fontFamily = momoFont(),
                        fontSize = LARGE,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {

                // OPTION 1
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.auth_subtext_loading),
                        color = onColorBackground,
                        fontFamily = momoFont(),
                        fontSize = LARGE,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(22.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp),
                        strokeWidth = 4.dp,
                        color = onColorBackground
                    )
                }*/

                // OPTION 2
                /*Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.auth_subtext_loading),
                        color = onColorBackground,
                        fontFamily = momoFont(),
                        fontSize = LARGE,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(22.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp),
                        strokeWidth = 4.dp,
                        color = onColorBackground
                    )
                }*/

                //OPTION 3
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.auth_header_loading),
                            color = onColorBackground,
                            fontFamily = momoFont(),
                            fontSize = LARGE,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(22.dp))
                        Text(
                            text = stringResource(R.string.auth_subtext_loading),
                            color = onColorBackgroundDarker,
                            fontFamily = momoFont(),
                            fontSize = LARGE,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.CenterEnd),
                        strokeWidth = 4.dp,
                        color = onColorBackground
                    )
                }
            }
        }
        Spacer(Modifier.height(86.dp))
        Button(
            isAnimated = false,
            additionalText = stringResource(R.string.auth_btn_additional_text_google),
            onClick = {
                if (state.isLoading) return@Button

                scope.launch {
                    authViewModel.onAuthEvent(AuthEvent.StartSignin)
                    try {
                        val authResult = googleUiClient.signInWithGoogle(activity)
                        val user = authResult.user
                        if (user != null) {
                            authViewModel.onAuthEvent(AuthEvent.SetSigninSuccess(user))
                        } else {
                            authViewModel.onAuthEvent(AuthEvent.SetSigninFail("Google sign in failed"))
                        }
                    } catch (e: Exception) {
                        authViewModel.onAuthEvent(AuthEvent.SetSigninFail(e.message ?: "Error"))
                    }
                }
            },
            icon = IconType.Drawable(Google),
            iconTint = Color.Unspecified,
            textFontSize = EXTRA_MEDIUM,
            text = stringResource(R.string.auth_btn_text_google)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (state.isLoading) return@Button

                scope.launch {
                    try {
                        authViewModel.onAuthEvent(AuthEvent.StartSignin)
                        val userResult = googleUiClient.signInGuest()
                        val user = userResult.user
                        if (user != null) {
                            authViewModel.onAuthEvent(AuthEvent.SetSigninSuccess(user))
                        } else {
                            authViewModel.onAuthEvent(AuthEvent.SetSigninFail("Guest sign in failed"))
                        }
                    } catch (e: Exception) {
                        authViewModel.onAuthEvent(AuthEvent.SetSigninFail(e.message ?: "Error"))
                    }
                }
            },
            icon = IconType.Vector(LogIn),
            textFontSize = EXTRA_MEDIUM,
            backgroundColor = onColorBackgroundDarker,
            text = stringResource(R.string.auth_btn_text_guest),
            textColor = onColorPrimary,
            iconTint = onColorPrimary,
            isAnimated = false
        )
    }
}





