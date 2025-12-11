package com.biernatmdev.simple_service.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.user.domain.UserRepository
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
                _state.update {
                    it.copy(
                        user = user,
                        isLoading = user == null,
                        error = null
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.ReloadUserDetails -> reloadUserDetails()
            ProfileEvent.SignOut -> signOut()
            is ProfileEvent.UpdateUserDetails -> updateData(event.user)
            is ProfileEvent.OnProfileOptionClick -> handleProfileOptionClick(event.profileOption)
        }
    }
    private fun handleProfileOptionClick(profileOption: ProfileOption){
        when(profileOption){
            ProfileOption.SIGNOUT -> signOut()
            else -> sendEffect(ProfileEffect.NavigateTo(profileOption.screen))
        }
    }

    private fun reloadUserDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val uid = userRepository.getCurrentUserId()
            if (uid != null) {
                userRepository.getUserDetails()
                    .onSuccess {
                        userRepository.startObservingUser(uid)
                    }
                    .onFailure { exception ->
                        handleException(exception)
                    }
            } else {
                signOut()
            }
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

    private fun updateData(user: User){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.updateUserDetails(user)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, user = user) }
                    sendEffect(ProfileEffect.ShowSnackbar(UiText.StringResource(R.string.snackbar_profile_update)))
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

    private fun handleException(exception: Throwable){
        val errorMessage: UiText = when (exception) {
            is UserException.NotSignedInException -> UiText.StringResource(R.string.user_exception_not_signed_in)
            is UserException.NotFoundException -> UiText.StringResource(R.string.user_exception_not_found)
            is UserException.AccessDeniedException -> UiText.StringResource(R.string.user_exception_access_denied)
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