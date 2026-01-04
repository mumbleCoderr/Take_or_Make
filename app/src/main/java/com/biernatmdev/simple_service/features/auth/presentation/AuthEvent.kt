package com.biernatmdev.simple_service.features.auth.presentation

import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.features.auth.domain.AuthMode
import com.google.firebase.auth.FirebaseUser

sealed interface AuthEvent {
    data object OnGuestSignInClick: AuthEvent
    data object OnGoogleSignInClick: AuthEvent
    data object OnEmailSignUpClick: AuthEvent
    data object OnEmailSignInClick: AuthEvent
    data object OnResetPasswordClick: AuthEvent
    data object OnCheckBoxClick: AuthEvent
    data class OnSwitchAuthMode(val authMode: AuthMode): AuthEvent
    data class OnGoogleSignInClickResult(val result: Result<FirebaseUser?>) : AuthEvent

    // TEXTFIELD CLEANER
    data object OnEmailFocused : AuthEvent
    data object OnPasswordFocused : AuthEvent
    data object OnPasswordRepeatFocused : AuthEvent
    data object OnFirstNameFocused : AuthEvent
    data object OnLastNameFocused : AuthEvent
}

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
    data object LaunchGoogleSignIn : AuthEffect
    data class ShowSnackbar(val message: UiText) : AuthEffect
}