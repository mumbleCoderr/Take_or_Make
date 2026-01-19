package com.biernatmdev.simple_service.features.favourites

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText

data class FavouritesState(
    val isPullRefreshing: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val offers: List<Offer> = emptyList(),
    val error: UiText? = null,
    val isEndOfList: Boolean = false
)