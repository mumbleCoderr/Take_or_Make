package com.biernatmdev.simple_service.features.offer_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OfferDetailsViewModel(
) : ViewModel(){

    private val _state = MutableStateFlow((OfferDetailsState()))
    val state = _state.asStateFlow()

    private val _effect = Channel<OfferDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: OfferDetailsEvent){
        when(event){
            OfferDetailsEvent.OnBackClick -> {
                sendEffect(OfferDetailsEffect.NavigateBack)
            }
            OfferDetailsEvent.OnBuyButtonClick -> {
                sendEffect(OfferDetailsEffect.NavigateToSuccessScreen)
            }
        }
    }

    private fun sendEffect(effect: OfferDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /*private fun handleException(exception: Throwable) {
        val errorMessage: UiText = when (exception) {

        }

        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage
            )
        }

        sendEffect(OfferDetailsEffect.ShowSnackbar(errorMessage))
    }*/
}