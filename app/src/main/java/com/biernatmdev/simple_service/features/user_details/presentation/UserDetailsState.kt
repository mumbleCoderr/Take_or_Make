package com.biernatmdev.simple_service.features.user_details.presentation

import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.features.user_details.domain.UserDetailsFormItem

data class UserDetailsState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val activeEditingItem: UserDetailsFormItem? = null,
    val textFieldErrors: Map<UserDetailsFormItem, UiText> = emptyMap(),
    val profilePicture: String = "",
    val isFormModified: Boolean = false,
)