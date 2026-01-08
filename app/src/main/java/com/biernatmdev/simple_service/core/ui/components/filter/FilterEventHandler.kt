package com.biernatmdev.simple_service.core.ui.components.filter

import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit

class FilterEventHandler {
    fun reduce(state: FilterState, event: FilterEvent): FilterState {
        return when (event) {
            FilterEvent.OnMainFilterDropdownClick -> {
                val isGoingToExpand = !state.isFilterDropdownExpanded
                state.copy(
                    isFilterDropdownExpanded = isGoingToExpand,
                    isTransactionTypeFilterDropdownExpanded = if (!isGoingToExpand) false else state.isTransactionTypeFilterDropdownExpanded,
                    isOfferTypeFilterDropdownExpanded = if (!isGoingToExpand) false else state.isOfferTypeFilterDropdownExpanded,
                    isPriceDropdownExpanded = if (!isGoingToExpand) false else state.isPriceDropdownExpanded,
                    isCategoryDropdownExpanded = if (!isGoingToExpand) false else state.isCategoryDropdownExpanded,
                )
            }

            FilterEvent.OnTransactionTypeFilterDropdownClick -> {
                state.copy(isTransactionTypeFilterDropdownExpanded = !state.isTransactionTypeFilterDropdownExpanded)
            }
            is FilterEvent.OnTransactionTypeClick -> {
                state.copy(
                    selectedTransactionType = event.transactionType,
                    isTransactionTypeFilterDropdownExpanded = false
                )
            }
            FilterEvent.OnOfferTypeFilterDropdownClick -> {
                state.copy(isOfferTypeFilterDropdownExpanded = !state.isOfferTypeFilterDropdownExpanded)
            }
            is FilterEvent.OnOfferTypeClick -> {
                state.copy(
                    selectedOfferType = event.offerType,
                    isOfferTypeFilterDropdownExpanded = false,
                    selectedSuperCategory = null,
                    selectedSubcategory = null,
                    isSuperCategoryDropdownExpanded = false,
                    isSubcategoryDropdownExpanded = false,
                    selectedItemCondition = null,
                )
            }
            FilterEvent.OnPriceFilterDropdownClick -> {
                state.copy(isPriceDropdownExpanded = !state.isPriceDropdownExpanded)
            }
            FilterEvent.OnPriceCurrencyFilterDropdownClick -> {
                state.copy(isCurrencyDropdownExpanded = !state.isCurrencyDropdownExpanded)
            }
            is FilterEvent.OnPriceCurrencyClick -> {
                state.copy(
                    selectedCurrency = event.priceCurrency,
                    isCurrencyDropdownExpanded = false
                )
            }
            FilterEvent.OnPriceCurrencyFilterDropdownDismiss -> {
                state.copy(isCurrencyDropdownExpanded = false)
            }
            FilterEvent.OnPriceUnitFilterDropdownClick -> {
                state.copy(isPriceUnitDropdownExpanded = !state.isPriceUnitDropdownExpanded)
            }
            is FilterEvent.OnPriceUnitClick -> {
                state.copy(
                    selectedPriceUnit = event.priceUnit,
                    isPriceUnitDropdownExpanded = false
                )
            }
            FilterEvent.OnPriceUnitFilterDropdownDismiss -> {
                state.copy(isPriceUnitDropdownExpanded = false)
            }
            FilterEvent.OnCategoryFilterDropdownClick -> {
                state.copy(isCategoryDropdownExpanded = !state.isCategoryDropdownExpanded)
            }
            FilterEvent.OnSuperCategoryFilterDropdownClick -> {
                state.copy(isSuperCategoryDropdownExpanded = !state.isSuperCategoryDropdownExpanded)
            }
            is FilterEvent.OnSuperCategoryClick -> {
                state.copy(
                    selectedSuperCategory = event.category,
                    selectedSubcategory = null,
                    isSuperCategoryDropdownExpanded = false
                )
            }
            FilterEvent.OnSuperCategoryFilterDropdownDismiss -> {
                state.copy(isSuperCategoryDropdownExpanded = false)
            }
            FilterEvent.OnSubcategoryFilterDropdownClick -> {
                state.copy(isSubcategoryDropdownExpanded = !state.isSubcategoryDropdownExpanded)
            }
            is FilterEvent.OnSubcategoryClick -> {
                state.copy(
                    selectedSubcategory = event.category,
                    isSubcategoryDropdownExpanded = false
                )
            }
            FilterEvent.OnSubcategoryFilterDropdownDismiss -> {
                state.copy(isSubcategoryDropdownExpanded = false)
            }
            FilterEvent.OnItemConditionFilterDropdownClick -> {
                state.copy(isItemConditionFilterDropdownExpanded = !state.isItemConditionFilterDropdownExpanded)
            }
            is FilterEvent.OnItemConditionFilterClick -> {
                state.copy(
                    selectedItemCondition = event.itemCondition,
                    isItemConditionFilterDropdownExpanded = false
                )
            }
            FilterEvent.OnItemConditionFilterDropdownDismiss -> {
                state.copy(isItemConditionFilterDropdownExpanded = false)
            }
            FilterEvent.OnApplyFiltersButtonClick -> {
                state.copy(isFilterDropdownExpanded = false)
            }
            FilterEvent.OnClearFiltersButtonClick -> {
                state.copy(
                    selectedTransactionType = null,
                    selectedOfferType = null,
                    selectedCurrency = "ANY",
                    selectedPriceUnit = OfferPriceUnit.ANY,
                    selectedSuperCategory = null,
                    selectedSubcategory = null,
                    selectedItemCondition = null,

                    isTransactionTypeFilterDropdownExpanded = false,
                    isOfferTypeFilterDropdownExpanded = false,
                    isPriceDropdownExpanded = false,
                    isCurrencyDropdownExpanded = false,
                    isPriceUnitDropdownExpanded = false,
                    isCategoryDropdownExpanded = false,
                    isSuperCategoryDropdownExpanded = false,
                    isSubcategoryDropdownExpanded = false,
                    isItemConditionFilterDropdownExpanded = false,

                    isFilterDropdownExpanded = false
                )
            }
            FilterEvent.OnSearchbarButtonClick,
            FilterEvent.OnDisabledCityClick,
            FilterEvent.OnDisabledItemConditionClick,
            FilterEvent.OnDisabledSubcategoryClick,
            FilterEvent.OnDisabledSuperCategoryClick -> state
        }
    }
}