package com.biernatmdev.simple_service.features.auth.presentation

import com.biernatmdev.simple_service.core.ui.model.UiText

data class AuthState(
    val isLoading: Boolean = false,
    val error: UiText? = null
)