package com.biernatmdev.simple_service.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.features.home.domain.HomeSubscreen

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedScreen: HomeSubscreen,
    onScreenSelect: (HomeSubscreen) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorBackground)
            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeSubscreen.entries.forEach { screenChip ->
                val isSelected = selectedScreen == screenChip

                val screenIcon = if (isSelected) {
                    screenChip.iconFocused
                } else {
                    screenChip.iconNotFocused
                }

                IconButton(
                    onClick = { onScreenSelect(screenChip) }
                ) {
                    when (val icon = screenIcon) {
                        is IconType.Vector -> {
                            Icon(
                                imageVector = icon.imageVector,
                                tint = onColorBackground,
                                contentDescription = screenChip.title.asString(),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        is IconType.Drawable -> {
                            Icon(
                                painter = painterResource(id = icon.id),
                                tint = onColorBackground,
                                contentDescription = screenChip.title.asString(),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}