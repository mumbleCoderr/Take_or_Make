package com.biernatmdev.simple_service.features.home.take_module

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.components.filter.FilterState

data class TakeState (
    val isPullRefreshing: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val offers: List<Offer> = emptyList(),
    val displayingOffers: List<Offer> = emptyList(),
    val error: UiText? = null,
    val isLoadingMore: Boolean = false,
    val isEndOfList: Boolean = false,
    val filterState: FilterState = FilterState()
)