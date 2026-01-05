package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

sealed interface MakeEvent {
    data object OnAddNewOfferButtonClick : MakeEvent
}

sealed interface MakeEffect {
    data object NavigateToWizard : MakeEffect
}