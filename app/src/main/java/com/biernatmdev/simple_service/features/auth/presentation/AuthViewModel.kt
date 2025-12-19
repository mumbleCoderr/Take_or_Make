package com.biernatmdev.simple_service.features.auth.presentation

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.ui.model.UiText
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.features.auth.domain.AuthLoadingTarget
import com.biernatmdev.simple_service.features.auth.domain.AuthMode
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

    val emailState = TextFieldState()
    val passwordState = TextFieldState()
    val passwordRepeatState = TextFieldState()
    val firstNameState = TextFieldState()
    val lastNameState = TextFieldState()

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.OnEmailSignUpClick -> {
                if (validateSignUpForm()) {
                    emailSignUp()
                }
            }

            AuthEvent.OnEmailSignInClick -> {
                if (validateSignInForm()) {
                    emailSignIn()
                }
            }

            AuthEvent.OnGuestSignInClick -> guestSignIn()
            AuthEvent.OnGoogleSignInClick -> {
                _state.update {
                    it.copy(
                        loadingTarget = AuthLoadingTarget.SIGN_IN_GOOGLE,
                        error = null
                    )
                }
                sendEffect(AuthEffect.LaunchGoogleSignIn)
            }


            AuthEvent.OnResetPasswordClick -> {
                if (validatePasswordReset()) {
                    sendPasswordResetLink()
                }
            }

            is AuthEvent.OnSwitchAuthMode -> {
                _state.update {
                    it.copy(
                        isCheckBoxChecked = false,
                        error = null,
                        emailError = null,
                        passwordError = null,
                        passwordRepeatError = null,
                        firstNameError = null,
                        lastNameError = null,
                        checkboxError = null,
                        isResetPasswordLinkSent = false,
                        authMode = event.authMode,
                    )
                }
            }

            AuthEvent.OnCheckBoxClick -> _state.update {
                it.copy(
                    isCheckBoxChecked = !it.isCheckBoxChecked,
                    checkboxError = null,
                )
            }

            is AuthEvent.OnGoogleSignInClickResult -> handleGoogleSignInResult(event.result)

            // TEXTFIELD CLEANER
            AuthEvent.OnEmailFocused -> _state.update { it.copy(emailError = null) }
            AuthEvent.OnPasswordFocused -> _state.update { it.copy(passwordError = null) }
            AuthEvent.OnPasswordRepeatFocused -> _state.update { it.copy(passwordRepeatError = null) }
            AuthEvent.OnFirstNameFocused -> _state.update { it.copy(firstNameError = null) }
            AuthEvent.OnLastNameFocused -> _state.update { it.copy(lastNameError = null) }
        }
    }

    private fun sendPasswordResetLink() {
        val email = emailState.text.toString().trim()

        viewModelScope.launch {
            userRepository.resetPassword(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isResetPasswordLinkSent = true,
                        )
                    }
                }
                .onFailure { exception ->
                    handleException(exception)
                }
        }
    }

    private fun validatePasswordReset(): Boolean {
        var isFormCorrect = true
        val email = emailState.text.toString().trim()
        var emailErrorMsg: UiText? = null

        if (email.isBlank()) {
            isFormCorrect = false
            emailErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_blank_error_msg)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isFormCorrect = false
            emailErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_incorrect_error_msg)
        }

        _state.update {
            it.copy(
                emailError = emailErrorMsg,
            )
        }

        return isFormCorrect
    }

    private fun validateSignInForm(): Boolean {
        var isFormCorrect = true
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()

        var emailErrorMsg: UiText? = null
        var passwordErrorMsg: UiText? = null

        if (email.isBlank()) {
            isFormCorrect = false
            emailErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_blank_error_msg)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isFormCorrect = false
            emailErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_incorrect_error_msg)
        }
        if (password.isBlank()) {
            isFormCorrect = false
            passwordErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_blank_error_msg)
        }

        _state.update {
            it.copy(
                emailError = emailErrorMsg,
                passwordError = passwordErrorMsg,
            )
        }

        return isFormCorrect
    }

    private fun validateSignUpForm(): Boolean {
        var isFormCorrect = true
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()
        val passwordRepeat = passwordRepeatState.text.toString()
        val firstName = firstNameState.text.toString().trim()
        val lastName = lastNameState.text.toString().trim()
        val isCheckBoxChecked = _state.value.isCheckBoxChecked

        var emailErrorMsg: UiText? = null
        var passwordErrorMsg: UiText? = null
        var passwordRepeatErrorMsg: UiText? = null
        var firstNameErrorMsg: UiText? = null
        var lastNameErrorMsg: UiText? = null
        var checkboxErrorMsg: UiText? = null

        if (email.isEmpty()) {
            isFormCorrect = false
            emailErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_blank_error_msg)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isFormCorrect = false
            emailErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_incorrect_error_msg)
        }
        if (password.isEmpty()) {
            isFormCorrect = false
            passwordErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_blank_error_msg)
        } else if (password.length < 6) {
            isFormCorrect = false
            passwordErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_too_short_error_msg)
        }
        if (passwordRepeat.isEmpty()) {
            isFormCorrect = false
            passwordRepeatErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_blank_error_msg)
        } else if (password != passwordRepeat) {
            isFormCorrect = false
            passwordErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_not_identical_error_msg)
            passwordRepeatErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_not_identical_error_msg)
        }
        if (firstName.isEmpty()) {
            isFormCorrect = false
            firstNameErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_firstname_blank_error_msg)
        } else if (firstName.any { !it.isLetter() && !it.isWhitespace() }) {
            isFormCorrect = false
            firstNameErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_firstname_incorrect_error_msg)
        } else if (firstName.length > 50) {
            isFormCorrect = false
            firstNameErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_firstname_too_long_error_msg)
        }
        if (lastName.isEmpty()) {
            isFormCorrect = false
            lastNameErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_lastname_blank_error_msg)
        } else if (lastName.any { !it.isLetter() && !it.isWhitespace() && it != '-' && it != '\'' && it != '.' }) {
            isFormCorrect = false
            lastNameErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_lastname_incorrect_error_msg)
        } else if (lastName.length > 50) {
            isFormCorrect = false
            lastNameErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_textfield_lastname_too_long_error_msg)
        }
        if (!isCheckBoxChecked) {
            isFormCorrect = false
            checkboxErrorMsg =
                UiText.StringResource(R.string.auth_screen_bottom_section_checkbox_not_checked)
        }

        _state.update {
            it.copy(
                emailError = emailErrorMsg,
                passwordError = passwordErrorMsg,
                passwordRepeatError = passwordRepeatErrorMsg,
                firstNameError = firstNameErrorMsg,
                lastNameError = lastNameErrorMsg,
                checkboxError = checkboxErrorMsg,
            )
        }

        return isFormCorrect
    }

    private fun emailSignIn() {
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()

        viewModelScope.launch {
            _state.update { it.copy(loadingTarget = AuthLoadingTarget.SIGN_IN_EMAIL) }
            val result = userRepository.signInWithEmail(email, password)
            result.onSuccess { user ->
                userRepository.startObservingUser(user.uid)
                sendEffect(AuthEffect.ShowSnackbar(UiText.DynamicString("Welcome, back!")))
                sendEffect(AuthEffect.NavigateToHome)
            }.onFailure { exception ->
                handleException(exception)
            }
        }
    }

    private fun emailSignUp() { //TODO FINISH IT
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()
        val firstName = firstNameState.text.toString().trim()
        val lastName = lastNameState.text.toString().trim()
        val termsAccepted = _state.value.isCheckBoxChecked

        viewModelScope.launch {
            _state.update { it.copy(loadingTarget = AuthLoadingTarget.SIGN_UP_EMAIL) }
            val result = userRepository.signUpWithEmail(email, password, firstName, lastName)
            result.onSuccess { user ->
                userRepository.startObservingUser(user.uid)
                sendEffect(AuthEffect.ShowSnackbar(UiText.DynamicString("Welcome, $firstName!")))
                sendEffect(AuthEffect.NavigateToHome)
            }.onFailure { exception ->
                handleException(exception)
            }
        }
    }

    private fun guestSignIn() {
        viewModelScope.launch {
            _state.update { it.copy(loadingTarget = AuthLoadingTarget.SIGN_IN_GUEST, error = null) }
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
                    _state.update { it.copy(loadingTarget = AuthLoadingTarget.SIGN_IN_GOOGLE) }
                    signIn(user)
                } else {
                    _state.update { it.copy(loadingTarget = AuthLoadingTarget.NONE) }
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
                _state.update { it.copy(loadingTarget = AuthLoadingTarget.NONE) }
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
        _state.update { it.copy(loadingTarget = AuthLoadingTarget.NONE) }

        when (exception) {

            is UserException.WrongCredentials -> {
                val errorMsg =
                    UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_password_wrong_error_msg)
                _state.update {
                    it.copy(
                        emailError = errorMsg,
                        passwordError = errorMsg,
                        error = null
                    )
                }
            }

            is UserException.WeakPassword -> {
                val errorMsg =
                    UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_too_short_error_msg)
                _state.update {
                    it.copy(
                        passwordError = errorMsg,
                        error = null
                    )
                }
            }

            is UserException.EmailAlreadyInUse -> {
                val errorMsg =
                    UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_taken_error_msg)
                _state.update {
                    it.copy(
                        emailError = errorMsg,
                        error = null
                    )
                }
            }

            is UserException.NotFoundException -> {
                val errorMsg =
                    UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_password_wrong_error_msg)
                _state.update {
                    it.copy(
                        emailError = errorMsg,
                        passwordError = errorMsg,
                        error = null
                    )
                }
            }

            is UserException.InvalidEmailFormat -> {
                val errorMsg =
                    UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_incorrect_error_msg)
                _state.update {
                    it.copy(
                        emailError = errorMsg,
                        error = null
                    )
                }
            }

            else -> {
                val errorMessage: UiText = when (exception) {
                    is UserException.NetworkError -> UiText.StringResource(R.string.snackbar_msg_info_auth_no_internet_connection)
                    is UserException.AccessDeniedException -> UiText.StringResource(R.string.snackbar_msg_info_auth_access_denied)
                    is UserException.NotSignedInException -> UiText.StringResource(R.string.snackbar_msg_info_auth_user_not_signed_in)
                    is UserException.TooManyRequests -> UiText.StringResource(R.string.snackbar_msg_info_auth_too_many_requests)
                    else -> UiText.DynamicString(exception.message ?: "External server error")
                }

                _state.update {
                    it.copy(
                        error = errorMessage
                    )
                }
                sendEffect(AuthEffect.ShowSnackbar(errorMessage))
            }
        }
    }
}