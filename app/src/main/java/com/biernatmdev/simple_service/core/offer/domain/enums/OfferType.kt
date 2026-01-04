package com.biernatmdev.simple_service.core.offer.domain.enums

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

enum class OfferType(val displayName: UiText) {
    PRODUCT(displayName = UiText.StringResource(R.string.offer_type_name_product)),
    SERVICE(displayName = UiText.StringResource(R.string.offer_type_name_service)),
}