package com.biernatmdev.simple_service.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground

@Composable
fun ErrorScreen(error: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Cancel,
            modifier = Modifier.size(222.dp),
            contentDescription = "ERROR"
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = error,
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = EXTRA_MEDIUM,
            fontWeight = FontWeight.Bold
        )
    }
}