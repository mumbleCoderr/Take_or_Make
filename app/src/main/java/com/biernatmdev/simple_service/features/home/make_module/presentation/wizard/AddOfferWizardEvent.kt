package com.biernatmdev.simple_service.features.home.make_module.presentation.wizard

import android.net.Uri
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.features.home.make_module.domain.AddOfferWizardStep

sealed interface AddOfferWizardEvent {
    data object OnNextStepClick : AddOfferWizardEvent
    data object OnPreviousStepClick : AddOfferWizardEvent
    data object OnCloseClick : AddOfferWizardEvent
    data object OnCreateOfferClick : AddOfferWizardEvent
    data object OnCurrencyDropdownClick : AddOfferWizardEvent
    data class OnCurrencySelected(val currencyCode: String) : AddOfferWizardEvent
    data object OnCurrencyDropdownDismiss : AddOfferWizardEvent
    data object OnPriceUnitDropdownClick : AddOfferWizardEvent
    data class OnPriceUnitSelected(val priceUnit: OfferPriceUnit) : AddOfferWizardEvent
    data object OnPriceUnitDropdownDismiss : AddOfferWizardEvent
    data class OnTransactionTypeClick(val transactionType: TransactionType) : AddOfferWizardEvent
    data class OnOfferTypeClick(val offerType: OfferType) : AddOfferWizardEvent
    data object ToggleSuperCategorySection : AddOfferWizardEvent
    data class OnSuperCategorySelected(val superCategory: OfferSuperCategory) : AddOfferWizardEvent
    data object ToggleCategorySection : AddOfferWizardEvent
    data class OnCategorySelected(val category: OfferCategory) : AddOfferWizardEvent
    data object OnItemConditionDropdownClick : AddOfferWizardEvent
    data class OnItemConditionSelected(val itemCondition: ItemCondition) : AddOfferWizardEvent
    data object OnItemConditionDropdownDismiss : AddOfferWizardEvent
    data object OnDescriptionFocused : AddOfferWizardEvent
    data object OnCityFocused : AddOfferWizardEvent
    data class OnPhotosSelected(val photos: List<Uri>) : AddOfferWizardEvent
    data class OnPhotoRemoved(val photo: Any) : AddOfferWizardEvent
    data class OnGoToStep(val step: AddOfferWizardStep) : AddOfferWizardEvent
    data object OnTitleFocused : AddOfferWizardEvent
    data object OnPriceFocused : AddOfferWizardEvent
    data object OnExitDialogDismiss : AddOfferWizardEvent
    data object OnExitDialogConfirm : AddOfferWizardEvent
    data class InitWithOffer(val offer: Offer) : AddOfferWizardEvent
}

sealed interface AddOfferWizardEffect {
    data object NavigateBack : AddOfferWizardEffect
    data object CreateOffer : AddOfferWizardEffect
    data class ShowSnackbar(val message: UiText) : AddOfferWizardEffect
}