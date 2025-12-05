package com.biernatmdev.simple_service.features.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_SMALL
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.features.home.domain.HomeSubscreen

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedScreen: HomeSubscreen,
    onScreenSelect: (HomeSubscreen) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorBackground)
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        HomeSubscreen.entries.forEach { screenChip ->
            val tint by animateColorAsState(
                targetValue = if (selectedScreen == screenChip) ColorPrimary else onColorBackground
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                IconButton(
                    onClick = { onScreenSelect(screenChip) }
                ) {
                    when (val icon = screenChip.icon) {
                        is IconType.Vector -> {
                            Icon(
                                imageVector = icon.imageVector,
                                tint = tint,
                                contentDescription = stringResource(id = screenChip.title),
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        is IconType.Drawable -> {
                            Icon(
                                painter = painterResource(id = icon.id),
                                tint = tint,
                                contentDescription = stringResource(id = screenChip.title),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(id = screenChip.title),
                    color = onColorBackground,
                    fontFamily = momoFont(),
                    fontSize = EXTRA_SMALL,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.2 * EXTRA_SMALL
                )
            }
        }
    }
}