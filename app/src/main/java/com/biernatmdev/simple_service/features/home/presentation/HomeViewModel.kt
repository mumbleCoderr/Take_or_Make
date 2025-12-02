package com.biernatmdev.simple_service.features.home.presentation

import androidx.lifecycle.ViewModel
import com.biernatmdev.simple_service.features.home.presentation.HomeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    // private val offerRepository: OfferRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeMode -> {
                _state.update { it.copy(mode = event.mode) }

                //TODO fetchuseroffers
                /*if(event.mode == HomeMode.MAKE){
                    fetchUserOffers()
                }else{
                    fetchOtherOffers(category: Category)
                }*/
            }
        }
    }

    /*private fun fetchUserOffers() {

    }

    private fun fetchOtherOffers(category: Category) {

    }*/
}