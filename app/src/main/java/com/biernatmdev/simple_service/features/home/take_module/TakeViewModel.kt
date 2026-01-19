package com.biernatmdev.simple_service.features.home.take_module

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.offer.domain.utils.filterOffers
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.models.UiText.*
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEvent
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEventHandler
import com.biernatmdev.simple_service.features.home.take_module.TakeEffect.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TakeViewModel(
    private val offerRepository: OfferRepository,
) : ViewModel(){

    private val _state = MutableStateFlow((TakeState()))
    val state = _state.asStateFlow()

    private val _effect = Channel<TakeEffect>()
    val effect = _effect.receiveAsFlow()

    private val filterHandler = FilterEventHandler()

    val searchbarState = TextFieldState()
    val priceFromState = TextFieldState()
    val priceToState = TextFieldState()
    val cityState = TextFieldState()

    init {
        fetchOffers(isNextPage = false, isSwipe = false)
    }

    fun onEvent(event: TakeEvent){
        when(event){
            is TakeEvent.Filter -> {
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
            TakeEvent.OnPullToRefresh -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isPullRefreshing = true,
                            isEndOfList = false,
                        )
                    }
                    delay(1000)
                    fetchOffers(isNextPage = false, isSwipe = true)
                }
            }
            TakeEvent.OnScreenRefresh -> {
                _state.update {
                    it.copy(
                        isEndOfList = false,
                    )
                }
                fetchOffers(isNextPage = false, isSwipe = false)
            }
            is TakeEvent.OnOfferClick -> {
                sendEffect(TakeEffect.NavigateToOfferDetails(event.offer))
            }
            is TakeEvent.OnFavouriteClick -> {
                val offer = event.offer
                val newIsFavoriteState = !offer.isFavourite

                _state.update { currentState ->
                    currentState.copy(
                        offers = currentState.offers.map {
                            if (it.id == offer.id) {
                                it.copy(isFavourite = newIsFavoriteState)
                            } else it
                        },
                        displayingOffers = currentState.displayingOffers.map {
                            if (it.id == offer.id){
                                it.copy(isFavourite = newIsFavoriteState)
                            } else it
                        }
                    )
                }

                viewModelScope.launch {
                    offerRepository.toggleFavorite(offer.id, newIsFavoriteState)
                        .onFailure { error ->
                            _state.update { currentState ->
                                currentState.copy(
                                    offers = currentState.offers.map {
                                        if (it.id == offer.id){
                                            it.copy(isFavourite = !newIsFavoriteState)
                                        } else it
                                    },
                                    displayingOffers = currentState.displayingOffers.map {
                                        if (it.id == offer.id){
                                            it.copy(isFavourite = !newIsFavoriteState)
                                        } else it
                                    }
                                )
                            }
                            handleException(error)
                        }
                }
            }

            TakeEvent.OnLoadNextPage -> {
                if (!_state.value.isLoadingNextPage && !_state.value.isPullRefreshing && !_state.value.isEndOfList) {
                    fetchOffers(isNextPage = true, isSwipe = false)
                }
            }
        }
    }

    private fun sendEffect(effect: TakeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
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

    private fun fetchOffers(isNextPage: Boolean, isSwipe: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isPullRefreshing = isSwipe,
                    isLoadingNextPage = isNextPage,
                    error = null
                )
            }

            val lastCreatedAt = if (isNextPage) {
                _state.value.offers.lastOrNull()?.createdAt
            } else {
                null
            }

            offerRepository.getAllOffers(lastCreatedAt)
                .onSuccess { newOffers ->
                    _state.update { currentState ->
                        val updatedList = if (isNextPage) {
                            currentState.offers + newOffers
                        } else {
                            newOffers
                        }

                        currentState.copy(
                            isPullRefreshing = false,
                            isLoadingNextPage = false,
                            offers = updatedList,
                            isEndOfList = newOffers.isEmpty(),
                        )
                    }

                    applyFilters()
                }
                .onFailure { error -> handleException(error) }
        }
    }

    private fun handleException(exception: Throwable) {
        val errorMessage: UiText = when (exception) {
            is OfferException.NetworkError -> UiText.StringResource(R.string.offer_exception_network)
            else -> UiText.DynamicString(exception.message ?: "Unknown error occurred")
        }

        _state.update {
            it.copy(
                isPullRefreshing = false,
                isLoadingNextPage = false,
                error = errorMessage
            )
        }

        sendEffect(TakeEffect.ShowSnackbar(errorMessage))
    }
}