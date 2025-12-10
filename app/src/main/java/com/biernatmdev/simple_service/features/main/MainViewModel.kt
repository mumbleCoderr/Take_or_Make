package com.biernatmdev.simple_service.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository
): ViewModel(){
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init{
        onEvent(MainEvent.CheckAuth)
    }

    fun onEvent(event: MainEvent){
        when(event) {
            MainEvent.CheckAuth -> checkCurrentUser()
        }
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val uid = userRepository.getCurrentUserId()

            if (uid != null) {
                userRepository.getUserDetails()
                    .onSuccess {
                        userRepository.startObservingUser(uid)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                startDestination = Screen.HomeGraph
                            )
                        }
                    }
                    .onFailure {
                        userRepository.signOut()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                startDestination = Screen.SplashScreen
                            )
                        }
                    }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        startDestination = Screen.SplashScreen
                    )
                }
            }
        }
    }
}