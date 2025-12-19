package com.biernatmdev.simple_service.features.auth.presentation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceCheckBox
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceSnackbar
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceTextField
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.AccountOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.BackFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.EmailOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.EmailSentOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Google
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.SignInFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.PasswordOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.AppBackgroundImage
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.features.auth.domain.AuthLoadingTarget
import com.biernatmdev.simple_service.features.auth.domain.AuthMode
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AuthScreen(
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val authViewModel: AuthViewModel = koinViewModel()
    val googleUiClient: GoogleUiClient = koinInject()

    val state by authViewModel.state.collectAsStateWithLifecycle()

    BackHandler(
        enabled = state.authMode != AuthMode.SIGN_IN,
        onBack = {
            when (state.authMode) {
                AuthMode.SIGN_UP -> authViewModel.onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_IN))
                AuthMode.STATUTE -> authViewModel.onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_UP))
                AuthMode.PASSWORD_RESET -> authViewModel.onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_IN))
                AuthMode.SIGN_IN -> {}
            }
        }
    )

    LaunchedEffect(Unit) {
        authViewModel.effect.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToHome -> navigateToHome()
                AuthEffect.LaunchGoogleSignIn -> {
                    scope.launch {
                        try {
                            val result = googleUiClient.signInWithGoogle(activity)
                            authViewModel.onEvent(
                                AuthEvent.OnGoogleSignInClickResult(
                                    Result.success(
                                        result.user
                                    )
                                )
                            )
                        } catch (e: Exception) {
                            authViewModel.onEvent(
                                AuthEvent.OnGoogleSignInClickResult(
                                    Result.failure(
                                        e
                                    )
                                )
                            )
                        }
                    }
                }

                is AuthEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }
            }
        }
    }

    AuthScreenContent(
        snackbar = snackbar,
        emailState = authViewModel.emailState,
        passwordState = authViewModel.passwordState,
        passwordRepeatState = authViewModel.passwordRepeatState,
        firstNameState = authViewModel.firstNameState,
        lastNameState = authViewModel.lastNameState,
        state = state,
        onEvent = authViewModel::onEvent
    )
}

@Composable
fun AuthScreenContent(
    snackbar: SnackbarHostState,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    passwordRepeatState: TextFieldState,
    firstNameState: TextFieldState,
    lastNameState: TextFieldState,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        AuthScreenTopSection(Modifier.align(Alignment.TopCenter))
        AuthScreenBottomSection(
            modifier = Modifier.align(Alignment.BottomCenter),
            emailState = emailState,
            passwordState = passwordState,
            passwordRepeatState = passwordRepeatState,
            nameState = firstNameState,
            lastNameState = lastNameState,
            emailErrorMsg = state.emailError,
            passwordErrorMsg = state.passwordError,
            passwordRepeatErrorMsg = state.passwordRepeatError,
            firstNameErrorMsg = state.firstNameError,
            lastNameErrorMsg = state.lastNameError,
            isCheckBoxChecked = state.isCheckBoxChecked,
            checkboxErrorMsg = state.checkboxError,
            isResetPasswordLinkSent = state.isResetPasswordLinkSent,
            authMode = state.authMode,
            loadingTarget = state.loadingTarget,
            onEvent = onEvent,
        )
        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.TopCenter),
            snackbar = { data ->
                SimpleServiceSnackbar(data)
            })
    }
}

@Composable
fun AuthScreenTopSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(AppBackgroundImage)
                .crossfade(true).build(),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, ColorBackground
                        ),
                    )
                )
        )
    }
}

@Composable
fun AuthScreenBottomSection(
    modifier: Modifier = Modifier,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    passwordRepeatState: TextFieldState,
    nameState: TextFieldState,
    lastNameState: TextFieldState,
    emailErrorMsg: UiText?,
    passwordErrorMsg: UiText?,
    passwordRepeatErrorMsg: UiText?,
    firstNameErrorMsg: UiText?,
    lastNameErrorMsg: UiText?,
    isCheckBoxChecked: Boolean,
    checkboxErrorMsg: UiText?,
    isResetPasswordLinkSent: Boolean,
    loadingTarget: AuthLoadingTarget,
    authMode: AuthMode,
    onEvent: (AuthEvent) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val animationSpeed = 300
        val availableHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = availableHeight)
                .animateContentSize(animationSpec = tween(animationSpeed))
                .clip(RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp))
                .background(ColorBackground)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))

            AnimatedContent(
                targetState = authMode,
                label = "TitleAnimation",
            ) { targetAuthMode ->
                Text(
                    text = when (targetAuthMode) {
                        AuthMode.SIGN_IN -> stringResource(R.string.auth_screen_bottom_section_title_sign_in)
                        AuthMode.SIGN_UP -> stringResource(R.string.auth_screen_bottom_section_title_sign_up)
                        AuthMode.STATUTE -> stringResource(R.string.auth_screen_bottom_section_title_statute)
                        AuthMode.PASSWORD_RESET -> stringResource(R.string.auth_screen_bottom_section_title_password_reset)
                    },
                    color = onColorBackground,
                    fontFamily = momoFont(),
                    fontSize = EXTRA_LARGE,
                    lineHeight = LineHeight.EXTRA_LARGE,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(22.dp))
            AnimatedContent(
                targetState = authMode,
                label = "BottomSectionAnimation",
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(animationSpeed)) { height -> height } +
                            fadeIn(animationSpec = tween(animationSpeed)))
                        .togetherWith(
                            slideOutVertically(animationSpec = tween(animationSpeed)) { height -> height } +
                                    fadeOut(animationSpec = tween(animationSpeed))
                        ).using(
                            SizeTransform(clip = false) { initialSize, targetSize ->
                                keyframes {
                                    durationMillis = animationSpeed
                                    initialSize at 0
                                    targetSize at animationSpeed
                                }
                            }
                        )
                }
            ) { targetAuthMode ->
                when (targetAuthMode) {
                    AuthMode.SIGN_IN -> {
                        Box(Modifier.fillMaxWidth()) {
                            BottomSectionSignInMode(
                                onEvent = onEvent,
                                emailState = emailState,
                                passwordState = passwordState,
                                loadingTarget = loadingTarget,
                                emailErrorMsg = emailErrorMsg,
                                passwordErrorMsg = passwordErrorMsg,
                            )
                        }
                    }

                    AuthMode.SIGN_UP -> Box(Modifier.fillMaxWidth()) {
                        BottomSectionSignUpMode(
                            onEvent = onEvent,
                            emailState = emailState,
                            passwordState = passwordState,
                            passwordRepeatState = passwordRepeatState,
                            firstNameState = nameState,
                            lastNameState = lastNameState,
                            emailErrorMsg = emailErrorMsg,
                            passwordErrorMsg = passwordErrorMsg,
                            passwordRepeatErrorMsg = passwordRepeatErrorMsg,
                            firstNameErrorMsg = firstNameErrorMsg,
                            lastNameErrorMsg = lastNameErrorMsg,
                            isCheckboxChecked = isCheckBoxChecked,
                            checkboxErrorMsg = checkboxErrorMsg,
                            loadingTarget = loadingTarget,
                        )
                    }

                    AuthMode.STATUTE -> {
                        Box(Modifier.height(availableHeight * 0.75f)) {
                            BottomSectionStatuteMode(
                                onEvent = onEvent
                            )
                        }
                    }

                    AuthMode.PASSWORD_RESET -> {
                        Box(Modifier.height(availableHeight * 0.5f)) {
                            BottomSectionPasswordResetMode(
                                onEvent = onEvent,
                                emailState = emailState,
                                emailErrorMsg = emailErrorMsg,
                                isResetPasswordLinkSent = isResetPasswordLinkSent,
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun BottomSectionPasswordResetMode(
    onEvent: (AuthEvent) -> Unit,
    emailState: TextFieldState,
    emailErrorMsg: UiText?,
    isResetPasswordLinkSent: Boolean,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        Spacer(Modifier.height(22.dp))
        AnimatedContent(
            targetState = isResetPasswordLinkSent,
            label = "ResetButtonAnimation",
            transitionSpec = {
                val animationSpeed = 300
                (slideInVertically(animationSpec = tween(animationSpeed)) { height -> height } +
                        fadeIn(animationSpec = tween(animationSpeed)))
                    .togetherWith(
                        slideOutVertically(animationSpec = tween(animationSpeed)) { height -> height } +
                                fadeOut(animationSpec = tween(animationSpeed))
                    )
            }
        ) { sent ->
            if (!sent) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.auth_screen_bottom_section_forgot_password_info),
                        fontSize = REGULAR,
                        lineHeight = LineHeight.REGULAR,
                        color = onColorBackground,
                        fontFamily = momoFont(),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(22.dp))
                    SimpleServiceTextField(
                        isEmail = true,
                        state = emailState,
                        onFocus = { onEvent(AuthEvent.OnEmailFocused) },
                        errorText = emailErrorMsg,
                        placeholder = stringResource(R.string.auth_screen_bottom_section_textfield_placeholder_email),
                        icon = IconType.Vector(EmailOutlined),
                    )
                    Spacer(Modifier.height(48.dp))
                    SimpleServiceButton(
                        text = stringResource(R.string.auth_screen_bottom_section_btn_password_reset),
                        isAnimated = false,
                        onClick = { onEvent(AuthEvent.OnResetPasswordClick) }
                    )
                    Spacer(Modifier.height(22.dp))
                    SimpleServiceButton(
                        text = stringResource(R.string.auth_screen_bottom_section_btn_back),
                        backgroundColor = ColorSecondary,
                        textColor = onColorBackgroundDarker,
                        iconTint = onColorBackgroundDarker,
                        isIconLeading = true,
                        isIconAtEdge = true,
                        icon = IconType.Vector(BackFilled),
                        isAnimated = false,
                        onClick = { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_IN)) }
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = EmailSentOutlined,
                        contentDescription = null,
                        modifier = Modifier.size(128.dp),
                        tint = ColorPrimary,
                    )
                    Spacer(Modifier.height(38.dp))
                    Text(
                        text = stringResource(R.string.auth_screen_bottom_section_forgot_password_header),
                        color = ColorPrimary,
                        fontSize = SEMI_LARGE,
                        fontFamily = momoFont(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.auth_screen_bottom_section_forgot_password_subtext),
                        color = onColorBackground,
                        fontSize = REGULAR,
                        lineHeight = LineHeight.REGULAR,
                        fontFamily = momoFont(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(48.dp))
                    SimpleServiceButton(
                        text = stringResource(R.string.auth_screen_bottom_section_btn_back),
                        isIconLeading = true,
                        isIconAtEdge = true,
                        icon = IconType.Vector(BackFilled),
                        isAnimated = false,
                        onClick = { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_IN)) }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSectionStatuteMode(
    onEvent: (AuthEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(R.string.auth_screen_bottom_section_statute_text),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = REGULAR,
            lineHeight = LineHeight.REGULAR,
            fontWeight = FontWeight.Normal,
        )
        Spacer(Modifier.height(32.dp))
        SimpleServiceButton(
            text = stringResource(R.string.auth_screen_bottom_section_btn_back),
            isIconLeading = true,
            isIconAtEdge = true,
            icon = IconType.Vector(BackFilled),
            isAnimated = false,
            onClick = { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_UP)) })
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun BottomSectionSignUpSignInModeCommon(
    emailState: TextFieldState,
    passwordState: TextFieldState,
    emailErrorMsg: UiText?,
    passwordErrorMsg: UiText?,
    onEvent: (AuthEvent) -> Unit,
) {
    SimpleServiceTextField(
        isEmail = true,
        state = emailState,
        onFocus = { onEvent(AuthEvent.OnEmailFocused) },
        errorText = emailErrorMsg,
        placeholder = stringResource(R.string.auth_screen_bottom_section_textfield_placeholder_email),
        icon = IconType.Vector(EmailOutlined)
    )
    Spacer(Modifier.height(22.dp))
    SimpleServiceTextField(
        isPassword = true,
        state = passwordState,
        onFocus = { onEvent(AuthEvent.OnPasswordFocused) },
        errorText = passwordErrorMsg,
        placeholder = stringResource(R.string.auth_screen_bottom_section_textfield_placeholder_password),
        icon = IconType.Vector(PasswordOutlined)
    )
}

@Composable
fun BottomSectionSignUpMode(
    onEvent: (AuthEvent) -> Unit,
    isCheckboxChecked: Boolean,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    passwordRepeatState: TextFieldState,
    firstNameState: TextFieldState,
    lastNameState: TextFieldState,
    emailErrorMsg: UiText?,
    passwordErrorMsg: UiText?,
    passwordRepeatErrorMsg: UiText?,
    firstNameErrorMsg: UiText?,
    lastNameErrorMsg: UiText?,
    checkboxErrorMsg: UiText?,
    loadingTarget: AuthLoadingTarget,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        BottomSectionSignUpSignInModeCommon(
            emailState = emailState,
            passwordState = passwordState,
            emailErrorMsg = emailErrorMsg,
            passwordErrorMsg = passwordErrorMsg,
            onEvent = onEvent,
        )
        Spacer(Modifier.height(22.dp))
        SimpleServiceTextField(
            isPassword = true,
            state = passwordRepeatState,
            onFocus = { onEvent(AuthEvent.OnPasswordRepeatFocused) },
            errorText = passwordRepeatErrorMsg,
            placeholder = stringResource(R.string.auth_screen_bottom_section_textfield_placeholder_password_confirm),
            icon = IconType.Vector(PasswordOutlined)
        )
        Spacer(Modifier.height(22.dp))
        SimpleServiceTextField(
            state = firstNameState,
            onFocus = { onEvent(AuthEvent.OnFirstNameFocused) },
            errorText = firstNameErrorMsg,
            placeholder = stringResource(R.string.auth_screen_bottom_section_textfield_placeholder_first_name),
            icon = IconType.Vector(AccountOutlined)
        )
        Spacer(Modifier.height(22.dp))
        SimpleServiceTextField(
            state = lastNameState,
            onFocus = { onEvent(AuthEvent.OnLastNameFocused) },
            errorText = lastNameErrorMsg,
            placeholder = stringResource(R.string.auth_screen_bottom_section_textfield_placeholder_last_name),
            icon = IconType.Vector(AccountOutlined)
        )
        Spacer(Modifier.height(26.dp))
        val text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Clickable(
                    tag = "checkbox_text",
                    linkInteractionListener = { onEvent(AuthEvent.OnCheckBoxClick) })
            ) {
                withStyle(
                    style = SpanStyle(
                        color = onColorBackground, textDecoration = TextDecoration.None
                    )
                ) {
                    append(
                        UiText.StringResource(R.string.auth_screen_bottom_section_checkbox_text)
                            .asString()
                    )
                }
            }
            withLink(
                LinkAnnotation.Clickable(
                    tag = "statute",
                    linkInteractionListener = { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.STATUTE)) })
            ) {
                withStyle(
                    style = SpanStyle(
                        color = ColorPrimary, textDecoration = TextDecoration.None
                    ),
                ) {
                    append(
                        UiText.StringResource(R.string.auth_screen_bottom_section_checkbox_text_statute)
                            .asString()
                    )
                }
            }
        }
        SimpleServiceCheckBox(
            onClick = { onEvent(AuthEvent.OnCheckBoxClick) },
            isChecked = isCheckboxChecked,
            text = text,
            errorText = checkboxErrorMsg,
        )
        Spacer(Modifier.height(32.dp))
        SimpleServiceButton(
            text = stringResource(R.string.auth_screen_bottom_section_title_sign_up),
            isAnimated = true,
            isLoading = loadingTarget == AuthLoadingTarget.SIGN_UP_EMAIL,
            onClick = { onEvent(AuthEvent.OnEmailSignUpClick) }
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.auth_screen_bottom_section_sign_up_tail_text))
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "Sign In",
                        linkInteractionListener = { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_IN)) })
                ) {
                    withStyle(
                        style = SpanStyle(
                            color = ColorPrimary, textDecoration = TextDecoration.None
                        ),
                    ) {
                        append(stringResource(R.string.auth_screen_bottom_section_title_sign_in))
                    }
                }
            },
            color = onColorBackground,
            fontSize = REGULAR,
            fontFamily = momoFont(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(22.dp))
    }
}

@Composable
fun BottomSectionSignInMode(
    onEvent: (AuthEvent) -> Unit,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    loadingTarget: AuthLoadingTarget,
    emailErrorMsg: UiText?,
    passwordErrorMsg: UiText?,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        BottomSectionSignUpSignInModeCommon(
            emailState = emailState,
            passwordState = passwordState,
            emailErrorMsg = emailErrorMsg,
            passwordErrorMsg = passwordErrorMsg,
            onEvent = onEvent,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.auth_screen_bottom_section_password_reset),
            color = ColorPrimary,
            fontFamily = momoFont(),
            fontSize = REGULAR,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.PASSWORD_RESET)) })
        Spacer(Modifier.height(32.dp))
        SimpleServiceButton(
            text = stringResource(R.string.auth_screen_bottom_section_title_sign_in),
            isAnimated = true,
            isLoading = loadingTarget == AuthLoadingTarget.SIGN_IN_EMAIL,
            onClick = { onEvent(AuthEvent.OnEmailSignInClick) })
        Spacer(Modifier.height(64.dp))
        Divider(
            text = stringResource(R.string.auth_screen_bottom_section_divider),
            color = onColorBackground
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleServiceButton(
                text = stringResource(R.string.auth_screen_bottom_section_btn_google),
                icon = IconType.Drawable(Google),
                isIconLeading = true,
                isLoading = loadingTarget == AuthLoadingTarget.SIGN_IN_GOOGLE,
                onClick = { onEvent(AuthEvent.OnGoogleSignInClick) },
                textFontSize = SEMI_LARGE,
                backgroundColor = ColorSecondary,
                textColor = onColorBackgroundDarker,
                isAnimated = true,
                iconTint = Color.Unspecified,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            SimpleServiceButton(
                text = stringResource(R.string.auth_screen_bottom_section_btn_guest),
                icon = IconType.Vector(SignInFilled),
                iconTint = onColorBackgroundDarker,
                isIconLeading = true,
                isLoading = loadingTarget == AuthLoadingTarget.SIGN_IN_GUEST,
                onClick = { onEvent(AuthEvent.OnGuestSignInClick) },
                textFontSize = SEMI_LARGE,
                backgroundColor = ColorSecondary,
                textColor = onColorBackgroundDarker,
                isAnimated = true,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.auth_screen_bottom_section_sign_in_tail_text))
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "Sign Up",
                        linkInteractionListener = { onEvent(AuthEvent.OnSwitchAuthMode(AuthMode.SIGN_UP)) })
                ) {
                    withStyle(
                        style = SpanStyle(
                            color = ColorPrimary, textDecoration = TextDecoration.None
                        ),
                    ) {
                        append(stringResource(R.string.auth_screen_bottom_section_title_sign_up))
                    }
                }
            },
            color = onColorBackground,
            fontSize = REGULAR,
            fontFamily = momoFont(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(22.dp))
    }
}

@Composable
fun Divider(
    text: String = "", color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f), color = color, thickness = 1.dp
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            color = color,
            fontFamily = momoFont(),
            fontSize = REGULAR,
            fontWeight = FontWeight.Normal,
        )
        Spacer(Modifier.width(16.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f), color = color, thickness = 1.dp
        )
    }
}