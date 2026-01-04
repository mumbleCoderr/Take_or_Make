package com.biernatmdev.simple_service.features.user_details.presentation

import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.features.user_details.domain.UserDetailsFormItem

sealed interface UserDetailsEvent {
    data object OnButtonBackClick : UserDetailsEvent
    data class OnUserDetailsFormItemClick(val item: UserDetailsFormItem) : UserDetailsEvent
    data object OnTextFieldCancelFocus : UserDetailsEvent
    data class OnNewPfpSelected(val profilePicture: String) : UserDetailsEvent
    data class OnTextFieldFocused(val item: UserDetailsFormItem) : UserDetailsEvent
    data class OnUpdateUserDetailsClick(val user: User) : UserDetailsEvent
    data object OnConfirmChangesButtonClick : UserDetailsEvent
}

sealed interface UserDetailsEffect {
    data class ShowSnackbar(val message: UiText) : UserDetailsEffect
    data object NavigateBack : UserDetailsEffect
}