package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SMALL
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.AppForegroundImage
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground

@Composable
fun SimpleServiceSnackbar(
    snackbarData: SnackbarData
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .statusBarsPadding(),
        color = ColorSecondary,
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(AppForegroundImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "snackbar icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = snackbarData.visuals.message,
                color = onColorBackground,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Normal,
                fontSize = SMALL,
                lineHeight = LineHeight.SMALL,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}