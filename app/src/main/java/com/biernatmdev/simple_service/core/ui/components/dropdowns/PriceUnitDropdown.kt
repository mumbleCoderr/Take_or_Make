package com.biernatmdev.simple_service.core.ui.components.dropdowns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorSurface
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun PriceUnitDropdown(
    selectedUnit: OfferPriceUnit,
    isExpanded: Boolean,
    onDropdownClick: () -> Unit,
    onUnitSelected: (OfferPriceUnit) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ColorSurface)
                .clickable { onDropdownClick() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedUnit.displayName.asString(),
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold,
                fontSize = MEDIUM,
                color = onColorBackgroundDarker
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (isExpanded) ArrowUp else ArrowDown,
                contentDescription = null,
                tint = onColorBackgroundDarker,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .background(ColorBackground)
                .heightIn(max = 300.dp)
        ) {
            OfferPriceUnit.entries.forEach { unit ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = unit.displayName.asString(),
                            fontFamily = momoFont(),
                            color = onColorBackground
                        )
                    },
                    onClick = { onUnitSelected(unit) }
                )
            }
        }
    }
}