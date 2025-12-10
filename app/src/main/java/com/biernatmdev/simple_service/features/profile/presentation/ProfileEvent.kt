package com.biernatmdev.simple_service.features.profile.presentation

import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.user.domain.model.User

sealed interface ProfileEvent {
    data object ReloadUserDetails : ProfileEvent
    data object SignOut : ProfileEvent
    data class UpdateUserDetails(val user: User) : ProfileEvent
}

sealed interface ProfileEffect {
    data object NavigateToAuth : ProfileEffect
    data class ShowSnackbar(val message: UiText) : ProfileEffect
}