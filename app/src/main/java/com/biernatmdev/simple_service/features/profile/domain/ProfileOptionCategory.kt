package com.biernatmdev.simple_service.features.profile.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

enum class ProfileOptionCategory(val category: UiText){
    GENERAL(category = UiText.StringResource(R.string.profile_option_category_general)),
    ACTIVITY(category = UiText.StringResource(R.string.profile_option_category_activity)),
    OTHER(category = UiText.StringResource(R.string.profile_option_category_other)),
}