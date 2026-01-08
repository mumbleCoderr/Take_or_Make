package com.biernatmdev.simple_service.features.offer_details

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText

sealed interface OfferDetailsEvent {
    data object OnBackClick : OfferDetailsEvent
    data class OnBuyButtonClick(val offer: Offer) : OfferDetailsEvent
    data object OnDisabledBuyButtonClick : OfferDetailsEvent
}

sealed interface OfferDetailsEffect {
    data class ShowSnackbar(val message: UiText) : OfferDetailsEffect
    data object NavigateBack : OfferDetailsEffect
    data object NavigateToSuccessScreen : OfferDetailsEffect
}