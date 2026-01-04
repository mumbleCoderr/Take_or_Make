package com.biernatmdev.simple_service.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.features.home.domain.HomeMode

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    selectedMode: HomeMode,
    onModeSelect: (HomeMode) -> Unit
) {
    val selectedIndex = HomeMode.entries.indexOf(selectedMode)

    SecondaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
            .fillMaxWidth()
            .background(ColorBackground)
            .statusBarsPadding()
            .clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)),
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedIndex),
                color = ColorPrimary,
            )
        },
        containerColor = ColorBackground,
        divider = {}
    ) {
        HomeMode.entries.forEachIndexed { index, mode ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onModeSelect(mode) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        when (val icon = mode.icon) {
                            is IconType.Vector -> {
                                Icon(
                                    imageVector = icon.imageVector,
                                    tint = onColorBackground,
                                    contentDescription = stringResource(mode.title),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            is IconType.Drawable -> {
                                Icon(
                                    painter = painterResource(id = icon.id),
                                    tint = onColorBackground,
                                    contentDescription = stringResource(mode.title),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = stringResource(mode.title),
                            color = onColorBackground,
                            fontFamily = momoFont(),
                            fontSize = MEDIUM,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            )
        }
    }
}