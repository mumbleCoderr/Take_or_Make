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
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.screens.ErrorScreen
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.ColorSurface
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SMALL
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Forward
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Profile
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.Profile_picture_background
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.Profile_picture_placeholder
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorPrimary
import com.biernatmdev.simple_service.core.user.domain.model.User
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

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoading) { //TODO move to @Composable fun
            CircularProgressIndicator()
        } else if (state.error != null) {
            ErrorScreen(state.error!!)
        } else if (state.user != null) {
            UserProfilePictureSection(state.user!!)
            Spacer(Modifier.height(48.dp))
            ProfileOptionItem(
                text = "idz w pizdu",
                iconAction = IconType.Drawable(Profile),
                onClick = { },
            )
            ProfileOptionItem(
                text = "idz w pizdu",
                iconAction = IconType.Drawable(Profile),
                onClick = { }
            )
            UserProfileDetailsSection(state.user!!)
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
            .fillMaxHeight(0.38f),
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
fun ProfileOptionItemList(){

}
@Composable
fun ProfileOptionItem(
    roundedCornerShapeValue: Dp = 12.dp,
    backgroundColor: Color = ColorSecondary,
    text: String,
    textColor: Color = onColorPrimary,
    textFont: FontFamily = momoFont(),
    textFontSize: TextUnit = MEDIUM,
    textFontWeight: FontWeight = FontWeight.Normal,
    iconAction: IconType,
    iconForward: IconType? = IconType.Vector(Forward),
    iconSize: Dp = 24.dp,
    iconTint: Color = onColorPrimary,
    spacerValue: Dp = 16.dp,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.9f)
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
                when (iconAction) {
                    is IconType.Vector -> {
                        Icon(
                            imageVector = iconAction.imageVector,
                            contentDescription = iconAction.imageVector.name,
                            tint = iconTint,
                            modifier = Modifier
                                .size(iconSize)
                        )
                    }

                    is IconType.Drawable -> {
                        Icon(
                            painter = painterResource(iconAction.id),
                            contentDescription = iconAction.id.toString(),
                            tint = iconTint,
                            modifier = Modifier
                                .size(iconSize)
                        )
                    }
                }
                Text(
                    text = text,
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

@Composable
fun UserProfileDetailsSection(
    user: User
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.profile_screen_header),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.profile_screen_header),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.profile_screen_header),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.profile_screen_header),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.profile_screen_header),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = MEDIUM,
            fontWeight = FontWeight.Bold
        )
    }
}

