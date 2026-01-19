package com.biernatmdev.simple_service.features.favourites

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText

sealed interface FavouritesEvent {
    data object OnScreenRefresh : FavouritesEvent
    data object OnPullToRefresh : FavouritesEvent
    data object OnLoadNextPage : FavouritesEvent
    data class OnOfferClick(val offer: Offer) : FavouritesEvent
    data class OnFavoriteClick(val offer: Offer) : FavouritesEvent
}

sealed interface FavouritesEffect {
    data class ShowSnackbar(val message: UiText) : FavouritesEffect
    data class NavigateToOfferDetails(val offer: Offer) : FavouritesEffect
}