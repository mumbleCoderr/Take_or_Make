package com.biernatmdev.simple_service.core.ui.components.dropdowns

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.offer.domain.enums.CategoryDisplayable
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.ColorSurface
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun CategoryDropdown(
    items: List<CategoryDisplayable>,
    selectedItem: CategoryDisplayable?,
    isExpanded: Boolean,
    onDropdownClick: () -> Unit,
    onItemSelected: (CategoryDisplayable) -> Unit,
    onDismiss: () -> Unit,
    placeholder: String,
    labelSize: TextUnit = MEDIUM,
    horizontalPadding: Dp = 16.dp,
    isEnabled: Boolean = true,
    onDisabledClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val background = if (isEnabled) ColorSecondary else ColorBackground
    val shape = RoundedCornerShape(16.dp)
    val borderStroke = if (!isEnabled) {
        BorderStroke(
            width = 1.dp,
            color = onColorBackgroundDarker,
        )
    } else {
        null
    }


    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .then(
                    if (borderStroke != null) {
                        Modifier.border(borderStroke, shape)
                    } else {
                        Modifier
                    }
                )
                .background(background, shape)
                .clip(shape)
                .clickable {
                    if (isEnabled) {
                        onDropdownClick()
                    } else {
                        onDisabledClick()
                    }
                }
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedItem?.displayName?.asString() ?: placeholder,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold,
                fontSize = labelSize,
                lineHeight = labelSize * 1.2,
                color = onColorBackgroundDarker,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (isExpanded) ArrowUp else ArrowDown,
                contentDescription = null,
                tint = onColorBackgroundDarker,
                modifier = Modifier.size(20.dp)
            )
        }

        if(isEnabled) {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onDismiss,
                modifier = Modifier
                    .background(ColorBackground)
                    .heightIn(max = 300.dp)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.displayName.asString(),
                                fontFamily = momoFont(),
                                color = onColorBackground
                            )
                        },
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }
    }
}