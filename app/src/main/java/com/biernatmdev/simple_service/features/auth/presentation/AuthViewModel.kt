package com.biernatmdev.simple_service.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _effect = Channel<AuthEffect>()
    val effect = _effect.receiveAsFlow()

    fun onAuthEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.StartSignin -> {
                _state.update { it.copy(isLoading = true) }
            }

            AuthEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }

            is AuthEvent.SetSigninFail -> {
                _state.update {
                    it.copy(isLoading = false, error = event.message)
                }
            }

            is AuthEvent.SetSigninSuccess -> {
                createUserInDatabase(event.user)
            }
        }
    }

    private fun createUserInDatabase(user: FirebaseUser) {
        viewModelScope.launch {
            val result = userRepository.createUser(user)

            _state.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                _effect.send(AuthEffect.NavigateToHome)
            } else {
                val msg = result.exceptionOrNull()?.message ?: "Database error"
                _state.update { it.copy(error = msg) }
            }
        }
    }
}

