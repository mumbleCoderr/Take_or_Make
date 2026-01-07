package com.biernatmdev.simple_service.core.ui.components.dropdowns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorSurface
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.core.utils.CurrencyUtils

@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    isExpanded: Boolean,
    onDropdownClick: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit,
    withAny: Boolean = false,
    labelSize: TextUnit = MEDIUM,
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
                text = selectedCurrency,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold,
                fontSize = labelSize,
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
            val currencies = remember(withAny) {
                CurrencyUtils.getPopularCurrencies().filter { currencyCode ->
                    withAny || currencyCode != "ANY"
                }
            }

            currencies.forEach { currencyCode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$currencyCode (${CurrencyUtils.getSymbol(currencyCode)})",
                            fontFamily = momoFont(),
                            color = onColorBackground
                        )
                    },
                    onClick = { onCurrencySelected(currencyCode) }
                )
            }
        }
    }
}