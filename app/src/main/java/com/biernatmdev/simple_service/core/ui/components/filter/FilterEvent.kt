package com.biernatmdev.simple_service.core.ui.components.filter

import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType

sealed interface FilterEvent {
    data object OnMainFilterDropdownClick : FilterEvent
    data object OnSearchbarButtonClick : FilterEvent
    data class OnTransactionTypeClick(val transactionType: TransactionType?) : FilterEvent
    data class OnOfferTypeClick(val offerType: OfferType?) : FilterEvent
    data object OnTransactionTypeFilterDropdownClick : FilterEvent
    data object OnOfferTypeFilterDropdownClick : FilterEvent
    data object OnPriceFilterDropdownClick : FilterEvent
    data object OnPriceCurrencyFilterDropdownClick : FilterEvent
    data class OnPriceCurrencyClick(val priceCurrency: String) : FilterEvent
    data object OnPriceCurrencyFilterDropdownDismiss : FilterEvent
    data class OnPriceUnitClick(val priceUnit: OfferPriceUnit) : FilterEvent
    data object OnPriceUnitFilterDropdownClick : FilterEvent
    data object OnPriceUnitFilterDropdownDismiss : FilterEvent
    data object OnCategoryFilterDropdownClick : FilterEvent
    data object OnSuperCategoryFilterDropdownClick : FilterEvent
    data object OnSuperCategoryFilterDropdownDismiss : FilterEvent
    data class OnSuperCategoryClick(val category: OfferSuperCategory) : FilterEvent
    data object OnSubcategoryFilterDropdownClick : FilterEvent
    data object OnSubcategoryFilterDropdownDismiss : FilterEvent
    data class OnSubcategoryClick(val category: OfferCategory) : FilterEvent
    data object OnDisabledSuperCategoryClick : FilterEvent
    data object OnDisabledSubcategoryClick : FilterEvent
    data object OnDisabledCityClick : FilterEvent
    data object OnItemConditionFilterDropdownClick : FilterEvent
    data class OnItemConditionFilterClick(val itemCondition: ItemCondition) : FilterEvent
    data object OnItemConditionFilterDropdownDismiss : FilterEvent
    data object OnDisabledItemConditionClick : FilterEvent
    data object OnClearFiltersButtonClick : FilterEvent
    data object OnApplyFiltersButtonClick : FilterEvent
}