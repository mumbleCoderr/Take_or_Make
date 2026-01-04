package com.biernatmdev.simple_service.core.offer.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

enum class OfferPriceUnit(val displayName: UiText) {
    ITEM(displayName = UiText.StringResource(R.string.offer_price_unit_name_item)),
    HOUR(displayName = UiText.StringResource(R.string.offer_price_unit_name_hour)),
    KM(displayName = UiText.StringResource(R.string.offer_price_unit_name_km)),
    PROJECT(displayName = UiText.StringResource(R.string.offer_price_unit_name_project)),
    SQUARE_METER(displayName = UiText.StringResource(R.string.offer_price_unit_name_square_meter)),
}