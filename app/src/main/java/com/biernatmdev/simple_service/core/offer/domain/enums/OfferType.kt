package com.biernatmdev.simple_service.core.offer.domain.enums

import android.os.Parcelable
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OfferType(val displayName: UiText) : Parcelable {
    PRODUCT(displayName = UiText.StringResource(R.string.offer_type_name_product)),
    SERVICE(displayName = UiText.StringResource(R.string.offer_type_name_service)),
}