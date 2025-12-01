package com.biernatmdev.simple_service.features.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.features.home.domain.BottomBarScreenChip
import com.biernatmdev.simple_service.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.ui.theme.ColorSurface

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selected: BottomBarScreenChip,
    onSelect: (BottomBarScreenChip) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorSurface)
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomBarScreenChip.entries.forEach { screenChip ->
            val tint by animateColorAsState(
                targetValue = if (selected == screenChip) ColorPrimary else ColorSecondary
            )
            IconButton(
                onClick = { onSelect(screenChip) }
            ) {
                Icon( // TODO CATEGORY AND PROFILE ICON CHANGE
                    imageVector = screenChip.icon,
                    tint = tint,
                    contentDescription = stringResource(id = screenChip.title),
                    modifier = Modifier
                        .size(64.dp)
                )
            }
        }
    }
}