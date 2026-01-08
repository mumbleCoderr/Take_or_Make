package com.biernatmdev.simple_service.features.offer_details

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText

data class OfferDetailsState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val currentUserId: String? = null
)