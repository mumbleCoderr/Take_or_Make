package com.biernatmdev.simple_service.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.core.data.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _uiEvent = MutableStateFlow<AuthUiEvent>(AuthUiEvent.Idle)
    val uiEvent: StateFlow<AuthUiEvent> = _uiEvent

    fun startLoading() {
        _uiEvent.value = AuthUiEvent.Loading
    }

    fun consumeEvent() {
        _uiEvent.value = AuthUiEvent.Idle
    }

    fun emitError(message: String) {
        _uiEvent.value = AuthUiEvent.Error(message)
    }

    fun onFirebaseUserSignIn(user: FirebaseUser) {
        _uiEvent.value = AuthUiEvent.Loading
        viewModelScope.launch {
            val result = userRepository.createUser(user)
            if (result.isSuccess) {
                _uiEvent.value = AuthUiEvent.Success
            } else {
                val message =
                    result.exceptionOrNull()?.message ?: "Something wrong with creating user"
                _uiEvent.value = AuthUiEvent.Error(message)
            }
        }
    }

}