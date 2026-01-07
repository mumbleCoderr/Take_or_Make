package com.biernatmdev.simple_service.core.ui.components.dropdowns

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
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
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorSurface
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker

@Composable
fun ItemConditionDropdown(
    selectedCondition: ItemCondition?,
    isExpanded: Boolean,
    onDropdownClick: () -> Unit,
    onConditionSelected: (ItemCondition) -> Unit,
    onDismiss: () -> Unit,
    onDisabledClick: () -> Unit = {},
    isEnabled: Boolean = true,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 0.dp,
    labelSize: TextUnit = MEDIUM,
    minHeight: Boolean = true,
    withAny: Boolean = false,
    placeholder: UiText = UiText.StringResource(R.string.item_condition_name),
    modifier: Modifier = Modifier
) {
    val background = if (isEnabled) ColorSurface else ColorBackground
    val shape = if(minHeight) RoundedCornerShape(16.dp) else RoundedCornerShape(14.dp)
    val borderStroke = if (!isEnabled) {
        BorderStroke(1.dp, onColorBackgroundDarker)
    } else {
        null
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .then(if (minHeight) Modifier.height(56.dp) else Modifier)
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
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedCondition?.displayName?.asString() ?: placeholder.asString(),
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold,
                fontSize = labelSize,
                lineHeight = labelSize * 1.2,
                color = onColorBackgroundDarker,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (isExpanded) ArrowUp else ArrowDown,
                contentDescription = null,
                tint = onColorBackgroundDarker,
                modifier = Modifier.size(20.dp)
            )
        }

        if (isEnabled) {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onDismiss,
                modifier = Modifier
                    .background(ColorBackground)
                    .heightIn(max = 300.dp)
            ) {
                ItemCondition.entries
                    .filter { condition ->
                        condition != ItemCondition.NOT_APPLICABLE && (withAny || condition != ItemCondition.ANY)
                    }
                    .forEach { condition ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = condition.displayName.asString(),
                                    fontFamily = momoFont(),
                                    color = onColorBackground
                                )
                            },
                            onClick = { onConditionSelected(condition) }
                        )
                    }
            }
        }
    }
}