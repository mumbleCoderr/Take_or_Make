package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.models.UiText.*
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeEffect.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MakeViewModel(
    private val offerRepository: OfferRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MakeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MakeEffect>()
    val effect = _effect.receiveAsFlow()

    val searchbarState = TextFieldState()
    val priceFromState = TextFieldState()
    val priceToState = TextFieldState()
    val cityState = TextFieldState()

    fun onEvent(event: MakeEvent) { //TODO NIE REFRESHOWAC PRZY WEJSCIU NA EKRAN, JEDYNIE PRZY TAKE MODULE TAK BEDZIE
        when (event) {
            is MakeEvent.OnAddNewOfferButtonClick -> {
                sendEffect(NavigateToWizard())
            }

            MakeEvent.OnScreenRefresh -> {
                fetchOffers()
            }

            MakeEvent.OnPullToRefresh -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    delay(1000)
                    fetchOffers()
                }
            }

            MakeEvent.OnMainFilterDropdownClick -> {
                _state.update {
                    val isGoingToExpand = !it.isFilterDropdownExpanded
                    it.copy(
                        isFilterDropdownExpanded = isGoingToExpand,
                        isTransactionTypeFilterDropdownExpanded = if (!isGoingToExpand) false else it.isTransactionTypeFilterDropdownExpanded,
                        isOfferTypeFilterDropdownExpanded = if (!isGoingToExpand) false else it.isOfferTypeFilterDropdownExpanded,
                        isPriceDropdownExpanded = if (!isGoingToExpand) false else it.isPriceDropdownExpanded,
                        isCategoryDropdownExpanded = if (!isGoingToExpand) false else it.isCategoryDropdownExpanded,
                    )
                }
            }

            MakeEvent.OnSearchbarButtonClick -> {
                filterOffersByTitle()
            }

            is MakeEvent.OnOfferTypeClick -> {
                cityState.clearText()
                _state.update {
                    it.copy(
                        selectedOfferType = event.offerType,
                        isOfferTypeFilterDropdownExpanded = false,
                        selectedSuperCategory = null,
                        selectedSubcategory = null,
                        isSuperCategoryDropdownExpanded = false,
                        isSubcategoryDropdownExpanded = false,
                        selectedItemCondition = null,
                    )
                }
            }
            is MakeEvent.OnTransactionTypeClick -> {
                _state.update {
                    it.copy(
                        selectedTransactionType = event.transactionType,
                        isTransactionTypeFilterDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnOfferTypeFilterDropdownClick -> {
                _state.update {
                    it.copy(isOfferTypeFilterDropdownExpanded = !it.isOfferTypeFilterDropdownExpanded)
                }
            }
            MakeEvent.OnTransactionTypeFilterDropdownClick -> {
                _state.update {
                    it.copy(isTransactionTypeFilterDropdownExpanded = !it.isTransactionTypeFilterDropdownExpanded)
                }
            }

            is MakeEvent.OnPriceCurrencyClick -> {
                _state.update {
                    it.copy(
                        selectedCurrency = event.priceCurrency,
                        isCurrencyDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnPriceCurrencyFilterDropdownClick -> {
                _state.update {
                    it.copy(isCurrencyDropdownExpanded = !it.isCurrencyDropdownExpanded)
                }
            }
            MakeEvent.OnPriceCurrencyFilterDropdownDismiss -> {
                _state.update { it.copy(isCurrencyDropdownExpanded = false) }
            }

            MakeEvent.OnPriceFilterDropdownClick -> {
                _state.update {
                    it.copy(isPriceDropdownExpanded = !it.isPriceDropdownExpanded)
                }
            }
            is MakeEvent.OnPriceUnitClick -> {
                _state.update {
                    it.copy(
                        selectedPriceUnit = event.priceUnit,
                        isPriceUnitDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnPriceUnitFilterDropdownClick -> {
                _state.update {
                    it.copy(isPriceUnitDropdownExpanded = !it.isPriceUnitDropdownExpanded)
                }
            }
            MakeEvent.OnPriceUnitFilterDropdownDismiss -> {
                _state.update { it.copy(isPriceUnitDropdownExpanded = false) }
            }

            MakeEvent.OnCategoryFilterDropdownClick -> {
                _state.update {
                    it.copy(isCategoryDropdownExpanded = !it.isCategoryDropdownExpanded)
                }
            }

            is MakeEvent.OnSubcategoryClick -> {
                _state.update {
                    it.copy(
                        selectedSubcategory = event.category,
                        isSubcategoryDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnSubcategoryFilterDropdownClick -> {
                _state.update {
                    it.copy(isSubcategoryDropdownExpanded = !it.isSubcategoryDropdownExpanded)
                }
            }
            MakeEvent.OnSubcategoryFilterDropdownDismiss -> {
                _state.update { it.copy(isSubcategoryDropdownExpanded = false) }
            }

            is MakeEvent.OnSuperCategoryClick -> {
                _state.update {
                    it.copy(
                        selectedSuperCategory = event.category,
                        selectedSubcategory = null,
                        isSuperCategoryDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnSuperCategoryFilterDropdownClick -> {
                _state.update {
                    it.copy(isSuperCategoryDropdownExpanded = !it.isSuperCategoryDropdownExpanded)
                }
            }
            MakeEvent.OnSuperCategoryFilterDropdownDismiss -> {
                _state.update { it.copy(isSuperCategoryDropdownExpanded = false) }
            }

            MakeEvent.OnDisabledSubcategoryClick -> {
                sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_subcategory)))
            }
            MakeEvent.OnDisabledSuperCategoryClick -> {
                sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_supercategory)))
            }

            MakeEvent.OnDisabledCityClick -> {
                sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_city)))
            }
            MakeEvent.OnDisabledItemConditionClick -> {
                sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_item_condition)))
            }
            is MakeEvent.OnItemConditionFilterClick -> {
                _state.update {
                    it.copy(
                        selectedItemCondition = event.itemCondition,
                        isItemConditionFilterDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnItemConditionFilterDropdownClick -> {
                _state.update {
                    it.copy(
                        isItemConditionFilterDropdownExpanded = !it.isItemConditionFilterDropdownExpanded,
                    )
                }
            }
            MakeEvent.OnItemConditionFilterDropdownDismiss -> {
                _state.update {
                    it.copy(
                        isItemConditionFilterDropdownExpanded = false,
                    )
                }
            }
            MakeEvent.OnApplyFiltersButtonClick -> {
                applyFilters()
            }
            MakeEvent.OnClearFiltersButtonClick -> {
                clearFilters()
            }
            is MakeEvent.OnOfferCardClick -> {
                _state.update {
                    it.copy(
                        isOfferCardPopUpIsVisible = !it.isOfferCardPopUpIsVisible,
                        selectedOffer = event.offer
                    )
                }
            }

            MakeEvent.OnOfferCardPopUpDismiss -> {
                _state.update {
                    it.copy(
                        isOfferCardPopUpIsVisible = false,
                    )
                }
            }

            is MakeEvent.OnDeleteOfferClick -> {
                _state.update {
                    it.copy(
                        isDeleteDialogVisible = true,
                        selectedOffer = event.offer
                    )
                }
            }
            is MakeEvent.OnEditOfferClick -> {
                _state.update {
                    it.copy(
                        isOfferCardPopUpIsVisible = false,
                    )
                }
                sendEffect(NavigateToWizard(event.offer))
            }
            MakeEvent.OnDeleteDialogConfirm -> {
                val offerToDelete = _state.value.selectedOffer
                if (offerToDelete != null) {
                    deleteOffer(offerToDelete)
                }
                _state.update {
                    it.copy(
                        isDeleteDialogVisible = false,
                    )
                }
            }
            MakeEvent.OnDeleteDialogDismiss -> {
                _state.update {
                    it.copy(
                        isDeleteDialogVisible = false,
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: MakeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun fetchOffers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val userId = userRepository.getCurrentUserId()
            if (userId != null) {
                offerRepository.getOffersByAuthorId(userId)
                    .onSuccess { fetchedOffers ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                offers = fetchedOffers,
                                displayingOffers = fetchedOffers
                            )
                        }
                    }
                    .onFailure { error ->
                        handleException(error)
                    }
            } else {
                handleException(UserException.NotSignedIn)
            }
        }
    }

    private fun filterOffersByTitle() {
        val query = searchbarState.text.toString().trim()

        if (query.isBlank()) {
            _state.update { it.copy(offers = it.displayingOffers) }
        } else {
            val filteredList = _state.value.displayingOffers.filter { offer ->
                offer.title.contains(query, ignoreCase = true)
            }

            _state.update { it.copy(offers = filteredList) }
        }
    }

    private fun applyFilters() {
        val currentState = _state.value
        val allOffers = currentState.offers

        val queryCity = cityState.text.toString().trim()
        val priceFrom = priceFromState.text.toString().toDoubleOrNull()
        val priceTo = priceToState.text.toString().toDoubleOrNull()

        val filteredList = allOffers.filter { offer ->

            if (currentState.selectedTransactionType != null && offer.transactionType != currentState.selectedTransactionType) {
                return@filter false
            }

            if (currentState.selectedOfferType != null && offer.offerType != currentState.selectedOfferType) {
                return@filter false
            }

            if (currentState.selectedSubcategory != null && currentState.selectedSubcategory != OfferCategory.ANY) {
                if (offer.subcategory != currentState.selectedSubcategory) return@filter false
            } else if (currentState.selectedSuperCategory != null && currentState.selectedSuperCategory != OfferSuperCategory.ANY) {
                if (offer.superCategory != currentState.selectedSuperCategory) return@filter false
            }

            if (currentState.selectedCurrency != "ANY") {
                if (!offer.currency.equals(currentState.selectedCurrency, ignoreCase = true)) return@filter false
            }

            if (currentState.selectedPriceUnit != OfferPriceUnit.ANY) {
                if (offer.priceUnit != currentState.selectedPriceUnit) return@filter false
            }

            if (priceFrom != null || priceTo != null) {
                val offerPrice = offer.price
                if (offerPrice == null) return@filter false

                if (priceFrom != null && offerPrice < priceFrom) return@filter false
                if (priceTo != null && offerPrice > priceTo) return@filter false
            }

            if (queryCity.isNotEmpty()) {
                if (!offer.city.contains(queryCity, ignoreCase = true)) return@filter false
            }

            if (currentState.selectedItemCondition != null) {
                if (offer.itemCondition != currentState.selectedItemCondition) return@filter false
            }

            true
        }

        _state.update {
            it.copy(
                displayingOffers = filteredList,
                isFilterDropdownExpanded = false,
            )
        }
    }

    private fun clearFilters() {
        priceFromState.clearText()
        priceToState.clearText()
        cityState.clearText()

        _state.update {
            it.copy(
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

                displayingOffers = it.offers,
                isFilterDropdownExpanded = false
            )
        }
    }

    private fun deleteOffer(offer: Offer) {
        viewModelScope.launch {

            offerRepository.deleteOffer(offer.id)
                .onSuccess {
                    _state.update { state ->
                        val updatedOffers = state.offers.filter { it.id != offer.id }

                        state.copy(
                            offers = updatedOffers,
                            selectedOffer = null,
                            isOfferCardPopUpIsVisible = false,
                            isDeleteDialogVisible = false
                        )
                    }
                    applyFilters()

                    sendEffect(ShowSnackbar(UiText.StringResource(R.string.make_module_snackbar_msg_delete_offer)))
                }
                .onFailure { error ->
                    handleException(error)
                }
        }
    }

    private fun handleException(exception: Throwable) {
        val errorMessage: UiText = when (exception) {
            is OfferException.NetworkError -> UiText.StringResource(R.string.offer_exception_network)
            is OfferException.AccessDenied -> UiText.StringResource(R.string.offer_exception_access_denied)
            is OfferException.NotFound -> UiText.StringResource(R.string.offer_exception_not_found)
            is UserException.NotSignedIn -> UiText.StringResource(R.string.offer_exception_user_not_signed_in)

            else -> UiText.DynamicString(exception.message ?: "Unknown error occurred")
        }

        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage
            )
        }

        sendEffect(MakeEffect.ShowSnackbar(errorMessage))
    }
}