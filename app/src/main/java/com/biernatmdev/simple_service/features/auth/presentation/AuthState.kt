package com.biernatmdev.simple_service.features.auth.presentation

import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.features.auth.domain.AuthMode

data class AuthState(
    val isLoading: Boolean = false,
    val authMode: AuthMode = AuthMode.SIGN_IN,
    val isCheckBoxChecked: Boolean = false,
    val error: UiText? = null,
)