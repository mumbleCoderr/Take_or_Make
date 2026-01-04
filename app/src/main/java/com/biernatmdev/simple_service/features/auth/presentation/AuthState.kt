package com.biernatmdev.simple_service.features.auth.presentation

import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.features.auth.domain.AuthLoadingTarget
import com.biernatmdev.simple_service.features.auth.domain.AuthMode

data class AuthState(
    val error: UiText? = null,

    val loadingTarget: AuthLoadingTarget = AuthLoadingTarget.NONE,
    val authMode: AuthMode = AuthMode.SIGN_IN,

    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val passwordRepeatError: UiText? = null,
    val firstNameError: UiText? = null,
    val lastNameError: UiText? = null,

    val checkboxError: UiText? = null,
    val isCheckBoxChecked: Boolean = false,

    val isResetPasswordLinkSent: Boolean = false,
){
    val isLoading: Boolean
        get() = loadingTarget != AuthLoadingTarget.NONE
}