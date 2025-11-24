package com.biernatmdev.simple_service.features.auth

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.features.components.Button
import com.biernatmdev.simple_service.features.components.rememberOvershootScales
import com.biernatmdev.simple_service.ui.theme.ColorBtnText
import com.biernatmdev.simple_service.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.ui.theme.ColorPrimaryText
import com.biernatmdev.simple_service.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.ui.theme.ColorSecondaryText
import com.biernatmdev.simple_service.ui.theme.ColorSurface
import com.biernatmdev.simple_service.ui.theme.ColorTertiary
import com.biernatmdev.simple_service.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Approval
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Campaign
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Construction
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Google
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Handshake
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.LogIn
import com.biernatmdev.simple_service.ui.theme.Resources.Icon.Sell
import com.biernatmdev.simple_service.ui.theme.momoFont

@Preview(showBackground = true)
@Composable
fun AuthScreen(){
    val iconSize = 180.dp
    val scales = rememberOvershootScales(count = 4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurface)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
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
        ){
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
        Text(
            text = stringResource(R.string.auth_header),
            color = ColorPrimaryText,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(R.string.auth_subtext),
            color = ColorSecondaryText,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(86.dp))
        Button(
            onClick = {},
            painterResource = Google,
            iconTint = Color.Unspecified,
            textFontSize = EXTRA_MEDIUM,
            text = stringResource(R.string.auth_btn_text_google)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {},
            imageVector = LogIn,
            textFontSize = EXTRA_MEDIUM,
            backgroundColor = ColorSecondaryText, //TODO CHANGE NAME OF THE COLOR
            text = stringResource(R.string.auth_btn_text_guest)
        )
    }
}





