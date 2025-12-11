package com.biernatmdev.simple_service.features.auth.presentation

import com.biernatmdev.simple_service.core.ui.model.UiText
import com.google.firebase.auth.FirebaseUser

sealed interface AuthEvent {
    data object OnGuestSignInClick: AuthEvent
    data object OnGoogleSignInClick: AuthEvent
    data class OnGoogleSignInClickResult(val result: Result<FirebaseUser?>) : AuthEvent
}

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
    data object LaunchGoogleSignIn : AuthEffect
    data class ShowSnackbar(val message: UiText) : AuthEffect
}