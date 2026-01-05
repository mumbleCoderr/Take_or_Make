package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MakeViewModel() : ViewModel() {
    private val _state = MutableStateFlow(MakeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MakeEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: MakeEvent) {
        when (event) {
            is MakeEvent.OnAddNewOfferButtonClick -> { sendEffect(MakeEffect.NavigateToWizard) }
        }
    }

    private fun sendEffect(effect: MakeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}