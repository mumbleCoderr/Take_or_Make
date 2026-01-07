package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText

sealed interface MakeEvent {
    data object OnAddNewOfferButtonClick : MakeEvent
    data object OnScreenRefresh : MakeEvent
    data object OnPullToRefresh : MakeEvent
    data object OnMainFilterDropdownClick : MakeEvent
    data object OnSearchbarButtonClick : MakeEvent
    data class OnTransactionTypeClick(val transactionType: TransactionType?) : MakeEvent
    data class OnOfferTypeClick(val offerType: OfferType?) : MakeEvent
    data object OnTransactionTypeFilterDropdownClick : MakeEvent
    data object OnOfferTypeFilterDropdownClick : MakeEvent
    data object OnPriceFilterDropdownClick : MakeEvent
    data object OnPriceCurrencyFilterDropdownClick : MakeEvent
    data class OnPriceCurrencyClick(val priceCurrency: String) : MakeEvent
    data object OnPriceCurrencyFilterDropdownDismiss : MakeEvent
    data class OnPriceUnitClick(val priceUnit: OfferPriceUnit) : MakeEvent
    data object OnPriceUnitFilterDropdownClick : MakeEvent
    data object OnPriceUnitFilterDropdownDismiss : MakeEvent
    data object OnCategoryFilterDropdownClick : MakeEvent
    data object OnSuperCategoryFilterDropdownClick : MakeEvent
    data object OnSuperCategoryFilterDropdownDismiss : MakeEvent
    data class OnSuperCategoryClick(val category: OfferSuperCategory) : MakeEvent

    data object OnSubcategoryFilterDropdownClick : MakeEvent
    data object OnSubcategoryFilterDropdownDismiss : MakeEvent
    data class OnSubcategoryClick(val category: OfferCategory) : MakeEvent
    data object OnDisabledSuperCategoryClick : MakeEvent
    data object OnDisabledSubcategoryClick : MakeEvent
    data object OnDisabledCityClick : MakeEvent
    data object OnItemConditionFilterDropdownClick : MakeEvent
    data class OnItemConditionFilterClick(val itemCondition: ItemCondition) : MakeEvent
    data object OnItemConditionFilterDropdownDismiss : MakeEvent
    data object OnDisabledItemConditionClick : MakeEvent
    data object OnClearFiltersButtonClick : MakeEvent
    data object OnApplyFiltersButtonClick : MakeEvent
    data class OnOfferCardClick(val offer: Offer) : MakeEvent
    data object OnOfferCardPopUpDismiss : MakeEvent
    data class OnEditOfferClick(val offer: Offer) : MakeEvent
    data class OnDeleteOfferClick(val offer: Offer) : MakeEvent
    data object OnDeleteDialogDismiss : MakeEvent
    data object OnDeleteDialogConfirm : MakeEvent
}

sealed interface MakeEffect {
    data class NavigateToWizard(val offer: Offer? = null) : MakeEffect
    data class ShowSnackbar(val message: UiText) : MakeEffect
}