package com.biernatmdev.simple_service.features.home.take_module

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEvent

sealed interface TakeEvent {
    data object OnScreenRefresh : TakeEvent
    data object OnPullToRefresh : TakeEvent
    data class OnOfferClick(val offer: Offer) : TakeEvent
    data class OnFavouriteClick(val offer: Offer) : TakeEvent
    data object OnLoadNextPage : TakeEvent
    data class Filter(val event: FilterEvent) : TakeEvent
}

sealed interface TakeEffect {
    data class ShowSnackbar(val message: UiText) : TakeEffect
    data class NavigateToOfferDetails(val offer: Offer) : TakeEffect
}