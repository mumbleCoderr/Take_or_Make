package com.biernatmdev.simple_service.features.auth

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null
)