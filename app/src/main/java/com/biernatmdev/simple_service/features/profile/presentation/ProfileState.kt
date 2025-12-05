package com.biernatmdev.simple_service.features.profile.presentation

import com.biernatmdev.simple_service.core.user.domain.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
)