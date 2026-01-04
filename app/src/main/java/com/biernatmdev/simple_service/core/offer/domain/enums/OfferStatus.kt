package com.biernatmdev.simple_service.core.offer.domain.enums

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

enum class OfferStatus(val displayName: UiText) {
    ACTIVE(displayName = UiText.StringResource(R.string.offer_status_name_active)),
    INACTIVE(displayName = UiText.StringResource(R.string.offer_status_name_inactive)),
}