package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.components.filter.FilterState

data class MakeState(
    val isPullRefreshing: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val offers: List<Offer> = emptyList(),
    val displayingOffers: List<Offer> = emptyList(),
    val error: UiText? = null,
    val isOfferCardPopUpIsVisible: Boolean = false,
    val selectedOffer: Offer? = null,
    val isDeleteDialogVisible: Boolean = false,
    val isEndOfList: Boolean = false,

    val filterState: FilterState = FilterState()
)
