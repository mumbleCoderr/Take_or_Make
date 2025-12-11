package com.biernatmdev.simple_service.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.UserException
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
    private val googleUiClient: GoogleUiClient
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _effect = Channel<AuthEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.OnGuestSignInClick -> guestSignIn()
            AuthEvent.OnGoogleSignInClick -> {
                _state.update { it.copy(isLoading = true, error = null) }
                sendEffect(AuthEffect.LaunchGoogleSignIn)
            }

            is AuthEvent.OnGoogleSignInClickResult -> handleGoogleSignInResult(event.result)
        }
    }

    private fun guestSignIn() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val result = googleUiClient.signInGuest()
                val user = result.user
                if (user != null) signIn(user)
                else throw UserException.NotFoundException
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleGoogleSignInResult(result: Result<FirebaseUser?>) {
        viewModelScope.launch {
            result.onSuccess { user ->
                if (user != null) {
                    _state.update { it.copy(isLoading = true) }
                    signIn(user)
                } else {
                    _state.update { it.copy(isLoading = false) }
                }
            }.onFailure { exception ->
                handleException(exception)
            }
        }
    }

    private suspend fun signIn(user: FirebaseUser) {
        userRepository.createUser(user)
            .onSuccess {
                userRepository.startObservingUser(user.uid)
                _state.update { it.copy(isLoading = false) }
                sendEffect(AuthEffect.NavigateToHome)
            }.onFailure { exception ->
                handleException(exception)
            }
    }

    private fun sendEffect(effect: AuthEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun handleException(exception: Throwable) {
        val errorMessage: UiText = when (exception) {
            is UserException.NotFoundException -> UiText.StringResource(R.string.user_exception_not_found)
            else -> {
                UiText.DynamicString(exception.message ?: "External server error")
            }
        }
        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage
            )
        }
        sendEffect(AuthEffect.ShowSnackbar(errorMessage))
    }
}

