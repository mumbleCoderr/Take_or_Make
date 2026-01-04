package com.biernatmdev.simple_service.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.features.profile.domain.ProfileOption
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                val isUserGuest = user?.isGuest ?: false
                _state.update {
                    it.copy(
                        user = user,
                        isUserGuest = isUserGuest,
                        isLoading = user == null,
                        error = null
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnProfileOptionClick -> handleProfileOptionClick(event.profileOption)
            ProfileEvent.TriggerLinkAccountAlert -> _state.update { it.copy(isLinkAccountAlertVisible = !it.isLinkAccountAlertVisible) }
            ProfileEvent.OnLinkAccountAlertConfirm -> handleLinkAccountAlertConfirm()
            ProfileEvent.TriggerSignOutAlert -> _state.update { it.copy(isSignOutAlertVisible = !it.isSignOutAlertVisible) }
            ProfileEvent.OnSignOutAlertConfirm -> handleSignOutAlertConfirm()
        }
    }

    private fun handleLinkAccountAlertConfirm() {
        _state.update { it.copy(isLinkAccountAlertVisible = false) }
        sendEffect(ProfileEffect.NavigateTo(Screen.UserDetailsScreen))
    }

    private fun handleSignOutAlertConfirm() {
        _state.update { it.copy(isSignOutAlertVisible = false) }
        signOut()
    }

    private fun handleProfileOptionClick(profileOption: ProfileOption) {
        when (profileOption) {
            ProfileOption.SIGN_OUT -> {
                if (_state.value.isUserGuest) {
                    onEvent(ProfileEvent.TriggerSignOutAlert)
                } else {
                    signOut()
                }
            }
            ProfileOption.LINK_ACCOUNT -> onEvent(ProfileEvent.TriggerLinkAccountAlert)
            else -> sendEffect(ProfileEffect.NavigateTo(profileOption.screen))
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.signOut()
                .onSuccess {
                    sendEffect(ProfileEffect.NavigateToAuth)
                }
                .onFailure { exception ->
                    handleException(exception)
                }
        }
    }

    private fun updateData(user: User) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.updateUserDetails(user)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, user = user) }
                    sendEffect(ProfileEffect.ShowSnackbar(UiText.StringResource(R.string.snackbar_msg_info_profile_update)))
                }
                .onFailure { exception ->
                    handleException(exception)
                }
        }
    }

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun handleException(exception: Throwable) {
        val errorMessage: UiText = when (exception) {
            is UserException.NotSignedIn -> UiText.StringResource(R.string.user_exception_not_signed_in)
            is UserException.NotFound -> UiText.StringResource(R.string.user_exception_not_found)
            is UserException.AccessDenied -> UiText.StringResource(R.string.user_exception_access_denied)
            is UserException.ValidationException -> UiText.DynamicString(exception.message)
            else -> UiText.DynamicString(exception.message ?: "External server error")
        }
        _state.update {
            it.copy(
                isLoading = false,
                error = errorMessage
            )
        }
        sendEffect(ProfileEffect.ShowSnackbar(errorMessage))
    }
}