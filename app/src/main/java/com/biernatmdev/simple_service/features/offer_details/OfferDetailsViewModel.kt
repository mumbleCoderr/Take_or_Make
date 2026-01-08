package com.biernatmdev.simple_service.features.offer_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.transaction.domain.model.Transaction
import com.biernatmdev.simple_service.core.transaction.domain.repository.TransactionRepository
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OfferDetailsViewModel(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : ViewModel(){

    private val _state = MutableStateFlow((OfferDetailsState()))
    val state = _state.asStateFlow()

    private val _effect = Channel<OfferDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        val userId = userRepository.getCurrentUserId()
        _state.update { it.copy(currentUserId = userId) }
    }

    fun onEvent(event: OfferDetailsEvent){
        when(event){
            OfferDetailsEvent.OnBackClick -> {
                sendEffect(OfferDetailsEffect.NavigateBack)
            }
            is OfferDetailsEvent.OnBuyButtonClick -> {
                createTransaction(event.offer)
            }

            OfferDetailsEvent.OnDisabledBuyButtonClick -> {
                sendEffect(OfferDetailsEffect.ShowSnackbar(UiText.StringResource(R.string.take_module_snackbar_msg_author_cant_buy)))
            }
        }
    }

    private fun sendEffect(effect: OfferDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun createTransaction(offer: Offer) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentUserId = userRepository.getCurrentUserId()

            if (currentUserId == null) {
                handleException(UserException.NotSignedIn)
                return@launch
            }

            if (currentUserId == offer.authorId) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }

            val newTransaction = Transaction(
                id = "",
                userOrderingOfferId = currentUserId,
                userAuthoringOfferId = offer.authorId,
                offerId = offer.id,
                createdAt = System.currentTimeMillis()
            )

            transactionRepository.createTransaction(newTransaction)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(OfferDetailsEffect.NavigateToSuccessScreen)
                }
                .onFailure { error ->
                    handleException(error)
                }
        }
    }

    private fun handleException(exception: Throwable) {
        val errorMessage: UiText = when (exception) {
            is UserException.NotSignedIn -> UiText.StringResource(R.string.user_exception_not_signed_in)
            else -> UiText.DynamicString(exception.message ?: "Unknown error")
        }

        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage
            )
        }

        sendEffect(OfferDetailsEffect.ShowSnackbar(errorMessage))
    }
}