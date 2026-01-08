package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.offer.domain.utils.filterOffers
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEvent
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEventHandler
import com.biernatmdev.simple_service.core.ui.models.UiText.*
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

    private val filterHandler = FilterEventHandler()

    val searchbarState = TextFieldState()
    val priceFromState = TextFieldState()
    val priceToState = TextFieldState()
    val cityState = TextFieldState()

    fun onEvent(event: MakeEvent) {
        when (event) {
            is MakeEvent.OnAddNewOfferButtonClick -> {
                sendEffect(NavigateToWizard())
            }

            MakeEvent.OnScreenRefresh -> {
                _state.update { it.copy(isEndOfList = false) }
                fetchOffers()
            }

            MakeEvent.OnPullToRefresh -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isEndOfList = false
                        )
                    }
                    delay(1000)
                    fetchOffers(isNextPage = false)
                }
            }

            is MakeEvent.Filter -> {
                val currentFilterState = _state.value.filterState

                val newFilterState = filterHandler.reduce(currentFilterState, event.event)

                _state.update { it.copy(filterState = newFilterState) }

                when (event.event) {
                    FilterEvent.OnApplyFiltersButtonClick,
                    FilterEvent.OnSearchbarButtonClick -> {
                        applyFilters()
                    }

                    FilterEvent.OnClearFiltersButtonClick -> {
                        searchbarState.clearText()
                        priceFromState.clearText()
                        priceToState.clearText()
                        cityState.clearText()
                        applyFilters()
                    }

                    FilterEvent.OnDisabledSubcategoryClick -> {
                        sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_subcategory)))
                    }
                    FilterEvent.OnDisabledSuperCategoryClick -> {
                        sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_supercategory)))
                    }
                    FilterEvent.OnDisabledCityClick -> {
                        sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_city)))
                    }
                    FilterEvent.OnDisabledItemConditionClick -> {
                        sendEffect(ShowSnackbar(StringResource(R.string.make_module_snackbar_msg_filter_item_condition)))
                    }

                    else -> {  }
                }
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

            MakeEvent.OnLoadNextPage -> {
                if (!_state.value.isLoading && !_state.value.isEndOfList) {
                    fetchOffers(isNextPage = true)
                }
            }
        }
    }

    private fun sendEffect(effect: MakeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun fetchOffers(isNextPage: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val userId = userRepository.getCurrentUserId()

            if (userId != null) {
                val lastCreatedAt = if (isNextPage) {
                    _state.value.offers.lastOrNull()?.createdAt
                } else {
                    null
                }

                offerRepository.getOffersByAuthorId(userId, lastCreatedAt)
                    .onSuccess { newOffers ->
                        _state.update { state ->
                            val updatedList = if (isNextPage) {
                                state.offers + newOffers
                            } else {
                                newOffers
                            }

                            state.copy(
                                isLoading = false,
                                offers = updatedList,
                                isEndOfList = newOffers.isEmpty(),
                            )
                        }
                        applyFilters()
                    }
                    .onFailure { error ->
                        handleException(error)
                    }
            } else {
                handleException(UserException.NotSignedIn)
            }
        }
    }

    private fun applyFilters() {
        val currentState = _state.value

        val filteredList = currentState.offers.filterOffers(
            filters = currentState.filterState,
            queryTitle = searchbarState.text.toString().trim(),
            queryCity = cityState.text.toString().trim(),
            priceFrom = priceFromState.text.toString().toDoubleOrNull(),
            priceTo = priceToState.text.toString().toDoubleOrNull()
        )

        _state.update {
            it.copy(displayingOffers = filteredList)
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