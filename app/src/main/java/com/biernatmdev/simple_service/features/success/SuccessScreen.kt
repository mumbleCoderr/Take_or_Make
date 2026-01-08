package com.biernatmdev.simple_service.features.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CelebrationOutlined
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun SuccessScreen(
    onBackButtonClick: () -> Unit,
) {

    SuccessScreenContent(
        onBackButtonClick = { onBackButtonClick() }
    )
}

@Composable
fun SuccessScreenContent(
    onBackButtonClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Congratulations",
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(36.dp))
        Icon(
            imageVector = CelebrationOutlined,
            tint = ColorPrimary,
            contentDescription = null,
            modifier = Modifier.size(160.dp)
        )
        Spacer(Modifier.height(36.dp))
        Text(
            text = "We wish everything will go well",
            color = onColorBackgroundDarker,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(120.dp))
        SimpleServiceButton(
            text = "Back to offers",
            icon = IconType.Vector(ArrowFilled),
            isIconAtEdge = true,
            isAnimated = false,
            onClick = { onBackButtonClick() },
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}