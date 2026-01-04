package com.biernatmdev.simple_service.features.user_details.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceDivider
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceSnackbar
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceTextField
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SMALL
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.BackFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.EditFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ForwardFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.AppForegroundImage
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.core.utils.ImageUtils
import com.biernatmdev.simple_service.features.user_details.domain.UserDetailsFormItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDetailsScreen(
    navigateBack: () -> Unit
) {
    val userDetailsViewModel: UserDetailsViewModel = koinViewModel()
    val state by userDetailsViewModel.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userDetailsViewModel.effect.collect { effect ->
            when (effect) {
                is UserDetailsEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }

                is UserDetailsEffect.NavigateBack -> navigateBack()
            }
        }
    }

    UserDetailsScreenContent(
        onEvent = userDetailsViewModel::onEvent,
        state = state,
        getTextFieldState = userDetailsViewModel::getTextFieldState,
        snackbar = snackbar,
    )
}

@Composable
fun UserDetailsScreenContent(
    state: UserDetailsState,
    onEvent: (UserDetailsEvent) -> Unit,
    getTextFieldState: (UserDetailsFormItem) -> TextFieldState,
    snackbar: SnackbarHostState,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    onEvent(UserDetailsEvent.OnTextFieldCancelFocus)
                })
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(102.dp))

            if (state.user != null) {
                UserDetailsPfpSection(
                    profilePicture = state.profilePicture,
                    onEvent = onEvent,
                )
                Spacer(Modifier.height(48.dp))
                FormList(
                    user = state.user,
                    onEvent = onEvent,
                    getTextFieldState = getTextFieldState,
                    activeEditingItem = state.activeEditingItem,
                    textFieldErrors = state.textFieldErrors
                )
                Spacer(Modifier.height(36.dp))
                SimpleServiceButton(
                    isAnimated = true,
                    isLoading = state.isLoading,
                    onClick = { onEvent(UserDetailsEvent.OnConfirmChangesButtonClick) },
                    text = if (state.user.isGuest) {
                        UiText.StringResource(R.string.profile_details_screen_btn_guest).asString()
                    } else {
                        UiText.StringResource(R.string.profile_details_screen_btn_signed).asString()
                    },
                    isEnabled = state.isFormModified,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(48.dp))
            }
        }
        TopBackgroundFade(Modifier.align(Alignment.TopCenter))
        UserDetailTopNavSection(
            onEvent = onEvent,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        BotBackgroundFade(Modifier.align(Alignment.BottomCenter))
        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.TopCenter),
            snackbar = { data ->
                SimpleServiceSnackbar(data)
            }
        )
    }
}

@Composable
fun TopBackgroundFade(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ColorBackground,
                        ColorBackground,
                        ColorBackground.copy(alpha = 0.9f),
                        Color.Transparent,
                    )
                )
            )
    )
}

@Composable
fun BotBackgroundFade(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        0.2f to Color.Transparent,
                        0.4f to ColorBackground.copy(alpha = 0.1f),
                        0.6f to ColorBackground.copy(alpha = 0.4f),
                        0.8f to ColorBackground.copy(alpha = 0.8f),
                        1.0f to ColorBackground
                    )
                )
            )
    )
}

@Composable
fun FormItem(
    user: User,
    onEvent: (UserDetailsEvent) -> Unit,
    isActive: Boolean,
    textFieldState: TextFieldState,
    item: UserDetailsFormItem,
    errorText: UiText?,
) {
    val focusRequester = remember { FocusRequester() }
    val isError = errorText != null

    AnimatedContent(
        targetState = isActive,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut())
            } else {
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut())
            }
        },
        label = "EditAnimation"
    ) { isEditing ->
        if (isEditing) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            SimpleServiceTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                isEmail = item == UserDetailsFormItem.EMAIL,
                isPhoneNumber = item == UserDetailsFormItem.PHONE_NUMBER,
                isPassword = item == UserDetailsFormItem.PASSWORD || item == UserDetailsFormItem.PASSWORD_REPEAT,
                roundedCornerShapeValue = 0.dp,
                state = textFieldState,
                onFocus = { onEvent(UserDetailsEvent.OnTextFieldFocused(item)) },
                errorText = errorText,
                placeholder = item.title.asString(),
                onDone = { onEvent(UserDetailsEvent.OnTextFieldCancelFocus) }
            )
        } else {
            val titleColor = if (isError) Color.Red else onColorBackground

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(UserDetailsEvent.OnUserDetailsFormItemClick(item)) }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = if (isError) 4.dp else 16.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.title.asString().uppercase(),
                                fontFamily = momoFont(),
                                fontSize = MEDIUM,
                                color = titleColor
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = textFieldState.text.toString(),
                                fontFamily = momoFont(),
                                fontSize = MEDIUM,
                                color = onColorBackgroundDarker
                            )
                        }
                        Icon(
                            imageVector = ForwardFilled,
                            tint = onColorBackgroundDarker,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(32.dp)
                        )
                    }

                    if (isError) {
                        Text(
                            text = errorText?.asString() ?: "",
                            fontFamily = momoFont(),
                            fontSize = SMALL,
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormList(
    modifier: Modifier = Modifier,
    user: User,
    onEvent: (UserDetailsEvent) -> Unit,
    getTextFieldState: (UserDetailsFormItem) -> TextFieldState,
    activeEditingItem: UserDetailsFormItem?,
    textFieldErrors: Map<UserDetailsFormItem, UiText>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        UserDetailsFormItem.entries.forEachIndexed { index, item ->
            val isPasswordField = item == UserDetailsFormItem.PASSWORD || item == UserDetailsFormItem.PASSWORD_REPEAT
            if (!user.isGuest && isPasswordField) {
                return@forEachIndexed
            }

            FormItem(
                user = user,
                onEvent = onEvent,
                item = item,
                isActive = activeEditingItem == item,
                textFieldState = getTextFieldState(item),
                errorText = textFieldErrors[item],
            )
            SimpleServiceDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                color = onColorBackgroundDarker,
            )
        }
    }
}

@Composable
fun UserDetailsPfpSection(
    modifier: Modifier = Modifier,
    profilePicture: String,
    onEvent: (UserDetailsEvent) -> Unit,
) {
    val context = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val base64String = ImageUtils.uriToBase64(context, uri)
                if (base64String != null) {
                    onEvent(UserDetailsEvent.OnNewPfpSelected(base64String))
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profilePicture.takeIf { it.isNotBlank() })
                .fallback(AppForegroundImage)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.profile_picture),
            modifier = Modifier
                .clip(CircleShape)
                .size(164.dp)
                .align(Alignment.Center)
        )
        IconButton(
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
        ) {
            Icon(
                imageVector = EditFilled,
                contentDescription = stringResource(R.string.edit),
                tint = onColorBackground,
            )
        }
    }
}

@Composable
fun UserDetailTopNavSection(
    modifier: Modifier = Modifier,
    onEvent: (UserDetailsEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
    ) {
        IconButton(
            onClick = { onEvent(UserDetailsEvent.OnButtonBackClick) },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            Icon(
                imageVector = BackFilled,
                tint = onColorBackground,
                contentDescription = stringResource(R.string.arrow_back),
                modifier = Modifier
                    .size(32.dp)
            )
        }
        Text(
            text = "Edit profile",
            fontFamily = momoFont(),
            fontSize = LARGE,
            color = onColorBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
