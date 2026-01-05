package com.biernatmdev.simple_service.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CloseFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.PhotoAddOutlined
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import kotlin.math.abs

@Composable
fun PhotoCarousel(
    photos: List<Any>,
    onRemoveClick: ((Any) -> Unit)? = null,
    onAddPhotoClick: (() -> Unit)? = null,
    onImageClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxPhotos = 5
    val showAddButton = onAddPhotoClick != null && photos.size < maxPhotos
    val pageCount = photos.size + if (showAddButton) 1 else 0
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 48.dp),
            pageSpacing = 8.dp,
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = 1f - (0.15f * abs(pageOffset)).coerceIn(0f, 1f)
            val alpha = 1f - (0.5f * abs(pageOffset)).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    },
                contentAlignment = Alignment.Center
            ) {
                if (page < photos.size) {
                    PhotoCard(
                        model = photos[page],
                        onRemoveClick = if (onRemoveClick != null) { { onRemoveClick(photos[page]) } } else null,
                        onClick = { onImageClick(photos[page]) },
                    )
                } else {
                    AddPhotoCard(
                        onClick = onAddPhotoClick ?: {}
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoCard(
    model: Any,
    onRemoveClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(ColorSecondary)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = model,
            contentDescription = UiText.StringResource(R.string.offer_photo).asString(),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (onRemoveClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onRemoveClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = CloseFilled,
                    contentDescription = UiText.StringResource(R.string.remove).asString(),
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AddPhotoCard(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(ColorSecondary)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = onColorBackgroundDarker.copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = PhotoAddOutlined,
                contentDescription = UiText.StringResource(R.string.add_more_photos).asString(),
                tint = ColorPrimary,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = UiText.StringResource(R.string.add_photo).asString(),
                fontFamily = momoFont(),
                color = onColorBackgroundDarker,
                fontWeight = FontWeight.Bold,
                fontSize = SEMI_LARGE
            )
        }
    }
}

@Composable
fun FullScreenImageViewer(
    imageModel: Any?,
    onDismiss: () -> Unit
) {
    if (imageModel != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = UiText.StringResource(R.string.full_screen_image).asString(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onDismiss() }
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .statusBarsPadding()
                ) {
                    Icon(
                        imageVector = CloseFilled,
                        contentDescription = UiText.StringResource(R.string.close).asString(),
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}