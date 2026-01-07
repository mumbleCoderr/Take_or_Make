package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText

data class MakeState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val offers: List<Offer> = emptyList(),
    val displayingOffers: List<Offer> = emptyList(),
    val error: UiText? = null,
    val isFilterDropdownExpanded: Boolean = false,
    val selectedTransactionType: TransactionType? = null,
    val selectedOfferType: OfferType? = null,
    val isTransactionTypeFilterDropdownExpanded: Boolean = false,
    val isOfferTypeFilterDropdownExpanded: Boolean = false,
    val isPriceDropdownExpanded: Boolean = false,
    val isCurrencyDropdownExpanded: Boolean = false,
    val selectedCurrency: String = "ANY",
    val isPriceUnitDropdownExpanded: Boolean = false,
    val selectedPriceUnit: OfferPriceUnit = OfferPriceUnit.ANY,
    val isCategoryDropdownExpanded: Boolean = false,
    val isSuperCategoryDropdownExpanded: Boolean = false,
    val selectedSuperCategory: OfferSuperCategory? = null,
    val isSubcategoryDropdownExpanded: Boolean = false,
    val selectedSubcategory: OfferCategory? = null,
    val selectedItemCondition: ItemCondition? = null,
    val isItemConditionFilterDropdownExpanded: Boolean = false,
    val isOfferCardPopUpIsVisible: Boolean = false,
    val selectedOffer: Offer? = null,
    val isDeleteDialogVisible: Boolean = false,
)
