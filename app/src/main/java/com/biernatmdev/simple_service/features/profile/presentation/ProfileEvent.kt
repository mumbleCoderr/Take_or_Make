package com.biernatmdev.simple_service.features.profile.presentation

import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.features.profile.domain.ProfileOption

sealed interface ProfileEvent {
    data class OnUpdateUserDetailsClick(val user: User) : ProfileEvent
    data class OnProfileOptionClick(val profileOption: ProfileOption) : ProfileEvent
    data object TriggerLinkAccountAlert : ProfileEvent
    data object OnLinkAccountAlertConfirm : ProfileEvent
}

sealed interface ProfileEffect {
    data object NavigateToAuth : ProfileEffect
    data class NavigateTo(val screen: Screen) : ProfileEffect
    data class ShowSnackbar(val message: UiText) : ProfileEffect
}