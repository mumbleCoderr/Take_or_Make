package com.biernatmdev.simple_service.core.offer.domain.enums

import android.os.Parcelable
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ItemCondition(val displayName: UiText) : Parcelable {
    ANY(displayName = UiText.StringResource(R.string.item_condition_name_any)),
    NEW(displayName = UiText.StringResource(R.string.item_condition_name_new)),
    USED(displayName = UiText.StringResource(R.string.item_condition_name_used)),
    DAMAGED(displayName = UiText.StringResource(R.string.item_condition_name_damaged)),
    NOT_APPLICABLE(displayName = UiText.StringResource(R.string.item_condition_name_not_app)),
}