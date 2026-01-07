package com.biernatmdev.simple_service.features.home.make_module.presentation.wizard

import android.net.Uri
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.features.home.make_module.domain.AddOfferWizardStep

data class AddOfferWizardState(
    val wizardStep: AddOfferWizardStep = AddOfferWizardStep.INFO_STEP,
    val titleError: UiText? = null,
    val priceError: UiText? = null,
    val transactionTypeError: UiText? = null,
    val offerTypeError: UiText? = null,
    val selectedTransactionType: TransactionType? = null,
    val selectedOfferType: OfferType? = null,
    val selectedCurrency: String = "PLN",
    val isCurrencyDropdownExpanded: Boolean = false,
    val selectedPriceUnit: OfferPriceUnit = OfferPriceUnit.HOUR,
    val isPriceUnitDropdownExpanded: Boolean = false,
    val selectedSuperCategory: OfferSuperCategory? = null,
    val isSuperCategoryExpanded: Boolean = false,
    val selectedCategory: OfferCategory? = null,
    val isCategoryExpanded: Boolean = false,
    val superCategoryError: UiText? = null,
    val categoryError: UiText? = null,
    val selectedItemCondition: ItemCondition = ItemCondition.NOT_APPLICABLE,
    val isItemConditionDropdownExpanded: Boolean = false,
    val descriptionError: UiText? = null,
    val cityError: UiText? = null,
    val selectedPhotos: List<Any> = emptyList(),
    val isLoading: Boolean = false,
    val generalError: UiText? = null,
    val isExitDialogVisible: Boolean = false,
    val isEditMode: Boolean = false,
    val editingOfferId: String? = null,
    val editingOfferAuthorId: String = "",
)