package com.biernatmdev.simple_service.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.screens.ErrorScreen
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.ColorSurface
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Forward
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.Profile_picture_background
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.Profile_picture_placeholder
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorSurface
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.features.profile.domain.ProfileOption
import com.biernatmdev.simple_service.features.profile.domain.ProfileOptionCategory
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navigateToAuth: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToAuth -> {
                    navigateToAuth()
                }

                is ProfileEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.LoadData)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.user != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UserProfilePictureSection(state.user!!)
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ProfileOptionList(category = ProfileOptionCategory.GENERAL)
                    Spacer(Modifier.height(24.dp))
                    ProfileOptionList(category = ProfileOptionCategory.ACTIVITY)
                }
            }
        }

        if (state.error != null && state.user == null) {
            ErrorScreen(
                error = state.error!!,
                isLoading = state.isLoading,
                onRetry = { viewModel.onEvent(ProfileEvent.LoadData) }
            )
        }

        val showErrorScreenLoader = state.error != null && state.user == null
        if (state.isLoading && !showErrorScreenLoader) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
            )
        }
    }
}

@Composable
fun UserProfilePictureSection(
    user: User
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Profile_picture_background)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "user profile picture background",
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            ColorSurface
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profilePicture)
                    .fallback(Profile_picture_placeholder)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "user profile picture background",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)
            )
            Spacer(Modifier.height(25.dp))
            Text(
                text = "${user.firstName} ${user.lastName}",
                color = onColorBackground,
                fontFamily = momoFont(),
                fontSize = SEMI_LARGE,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun ProfileOptionList(category: ProfileOptionCategory? = null) {
    val profileOptions = if (category == null) {
        ProfileOption.entries
    } else {
        ProfileOption.entries.filter { it.category == category }
    }

    profileOptions.forEachIndexed { index, item ->
        ProfileOption(
            item = item,
            isFirst = index == 0,
            isLast = index == profileOptions.size - 1
        ) {}
    }
}

@Composable
fun ProfileOption(
    item: ProfileOption,
    roundedCornerShapeValue: Dp = 12.dp,
    backgroundColor: Color = ColorSecondary,
    textColor: Color = onColorSurface,
    textFont: FontFamily = momoFont(),
    textFontSize: TextUnit = MEDIUM,
    textFontWeight: FontWeight = FontWeight.Normal,
    iconForward: IconType? = IconType.Vector(Forward),
    iconSize: Dp = 24.dp,
    iconTint: Color = onColorSurface,
    spacerValue: Dp = 16.dp,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    isMaxWidth: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(if (isMaxWidth) 1f else 0.9f)
            .clickable { onClick() },
        shape = if (isFirst) {
            RoundedCornerShape(
                topStart = roundedCornerShapeValue,
                topEnd = roundedCornerShapeValue
            )
        } else if (isLast) {
            RoundedCornerShape(
                bottomStart = roundedCornerShapeValue,
                bottomEnd = roundedCornerShapeValue
            )
        } else {
            RoundedCornerShape(0.dp)
        },
        color = backgroundColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = spacerValue),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(spacerValue),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (item.icon) {
                    is IconType.Vector -> {
                        Icon(
                            imageVector = item.icon.imageVector,
                            contentDescription = item.title.asString(),
                            tint = iconTint,
                            modifier = Modifier
                                .size(iconSize)
                        )
                    }

                    is IconType.Drawable -> {
                        Icon(
                            painter = painterResource(item.icon.id),
                            contentDescription = item.title.asString(),
                            tint = iconTint,
                            modifier = Modifier
                                .size(iconSize)
                        )
                    }
                }
                Text(
                    text = item.title.asString(),
                    color = textColor,
                    fontFamily = textFont,
                    fontSize = textFontSize,
                    fontWeight = textFontWeight,
                )
            }
            when (iconForward) {
                is IconType.Vector -> {
                    Icon(
                        imageVector = iconForward.imageVector,
                        contentDescription = iconForward.imageVector.name,
                        tint = iconTint,
                        modifier = Modifier
                            .size(iconSize)
                            .align(Alignment.CenterEnd)
                    )
                }

                is IconType.Drawable -> {
                    Icon(
                        painter = painterResource(iconForward.id),
                        contentDescription = iconForward.id.toString(),
                        tint = iconTint,
                        modifier = Modifier
                            .size(iconSize)
                            .align(Alignment.CenterEnd)
                    )
                }

                null -> TODO()
            }
        }
    }
}
/*@Composable
fun UserProfilePictureSection(
    user: User,
) {
    Text(
        text = stringResource(R.string.profile_screen_header),
        color = onColorBackground,
        fontFamily = momoFont(),
        fontSize = EXTRA_LARGE,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start
    )
    Spacer(Modifier.height(48.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.profilePicture)
                .crossfade(true)
                .fallback(Profile_picture_placeholder)
                .build(),
            contentDescription = "user profile picture",
            modifier = Modifier
                .clip(CircleShape)
                .size(200.dp)
        )
    }
}*/


