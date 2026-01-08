package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEvent

sealed interface MakeEvent {
    data object OnAddNewOfferButtonClick : MakeEvent
    data object OnScreenRefresh : MakeEvent
    data object OnPullToRefresh : MakeEvent
    data class OnOfferCardClick(val offer: Offer) : MakeEvent
    data object OnOfferCardPopUpDismiss : MakeEvent
    data class OnEditOfferClick(val offer: Offer) : MakeEvent
    data class OnDeleteOfferClick(val offer: Offer) : MakeEvent
    data object OnDeleteDialogDismiss : MakeEvent
    data object OnDeleteDialogConfirm : MakeEvent
    data object OnLoadNextPage : MakeEvent
    data class Filter(val event: FilterEvent) : MakeEvent
}

sealed interface MakeEffect {
    data class NavigateToWizard(val offer: Offer? = null) : MakeEffect
    data class ShowSnackbar(val message: UiText) : MakeEffect
}