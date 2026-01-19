package com.biernatmdev.simple_service.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val offerRepository: OfferRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesState())
    val state = _state.asStateFlow()

    private val _effect = Channel<FavouritesEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        fetchFavorites(isNextPage = false, isSwipe = false)
    }

    fun onEvent(event: FavouritesEvent) {
        when (event) {
            FavouritesEvent.OnScreenRefresh -> {
                _state.update { it.copy(isEndOfList = false) }
                fetchFavorites(isNextPage = false, isSwipe = false)
            }

            FavouritesEvent.OnPullToRefresh -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isPullRefreshing = true,
                            isEndOfList = false
                        )
                    }
                    delay(1000)
                    fetchFavorites(isNextPage = false, isSwipe = true)
                }
            }

            FavouritesEvent.OnLoadNextPage -> {
                if (!_state.value.isLoadingNextPage && !_state.value.isPullRefreshing && !_state.value.isEndOfList) {
                    fetchFavorites(isNextPage = true, isSwipe = false)
                }
            }

            is FavouritesEvent.OnOfferClick -> {
                sendEffect(FavouritesEffect.NavigateToOfferDetails(event.offer))
            }

            is FavouritesEvent.OnFavoriteClick -> {
                handleFavoriteToggle(event.offer)
            }
        }
    }

    private fun fetchFavorites(isNextPage: Boolean, isSwipe: Boolean) {
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

            offerRepository.getFavoriteOffers(lastCreatedAt)
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
                            isEndOfList = newOffers.isEmpty()
                        )
                    }
                }
                .onFailure { error ->
                    handleException(error)
                }
        }
    }

    private fun handleFavoriteToggle(offer: Offer) {
        val newIsFavoriteState = !offer.isFavourite

        _state.update { currentState ->
            currentState.copy(
                offers = currentState.offers.filter { it.id != offer.id }
            )
        }

        viewModelScope.launch {
            offerRepository.toggleFavorite(offer.id, newIsFavoriteState)
                .onFailure { error ->
                    _state.update { currentState ->
                        val restoredOffer = offer.copy(isFavourite = !newIsFavoriteState)
                        currentState.copy(
                            offers = (currentState.offers + restoredOffer).sortedByDescending { it.createdAt }
                        )
                    }
                    handleException(error)
                }
        }
    }

    private fun sendEffect(effect: FavouritesEffect) {
        viewModelScope.launch {
            _effect.send(effect)
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
        sendEffect(FavouritesEffect.ShowSnackbar(errorMessage))
    }
}