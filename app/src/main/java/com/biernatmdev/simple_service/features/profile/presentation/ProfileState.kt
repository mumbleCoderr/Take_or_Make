package com.biernatmdev.simple_service.features.profile.presentation

import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.user.domain.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val user: User? = null,
)