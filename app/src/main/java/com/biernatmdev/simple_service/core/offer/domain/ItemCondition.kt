package com.biernatmdev.simple_service.core.offer.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

enum class ItemCondition(val displayName: UiText) {
    NEW(displayName = UiText.StringResource(R.string.item_condition_name_new)),
    USED(displayName = UiText.StringResource(R.string.item_condition_name_used)),
    DAMAGED(displayName = UiText.StringResource(R.string.item_condition_name_damaged)),
    NOT_APPLICABLE(displayName = UiText.StringResource(R.string.item_condition_name_not_app)),
}