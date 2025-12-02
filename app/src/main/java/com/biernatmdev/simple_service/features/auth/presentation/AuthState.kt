package com.biernatmdev.simple_service.features.auth.presentation

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null
)