package com.biernatmdev.simple_service.features.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.data.validation.UserValidator
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.features.auth.domain.AuthLoadingTarget
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
) : ViewModel() { //TODO TWICE ERROR MSG ASSIGN?
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
        val email = emailState.text.toString().trim()
        val emailErrorMsg: UiText? = UserValidator.validateEmail(email)

        _state.update {
            it.copy(
                emailError = emailErrorMsg,
            )
        }

        return emailErrorMsg == null
    }

    private fun validateSignInForm(): Boolean {
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()

        val emailErrorMsg: UiText? = UserValidator.validateEmail(email)
        val passwordErrorMsg: UiText? = UserValidator.validatePassword(password)

        val errorMsgList = listOfNotNull(
            emailErrorMsg,
            passwordErrorMsg,
        )

        _state.update {
            it.copy(
                emailError = emailErrorMsg,
                passwordError = passwordErrorMsg,
            )
        }

        return errorMsgList.isEmpty()
    }

    private fun validateSignUpForm(): Boolean {
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()
        val passwordRepeat = passwordRepeatState.text.toString()
        val firstName = firstNameState.text.toString().trim()
        val lastName = lastNameState.text.toString().trim()
        val isCheckBoxChecked = _state.value.isCheckBoxChecked

        val emailErrorMsg: UiText? = UserValidator.validateEmail(email)
        val passwordErrorMsg: UiText? = UserValidator.validatePassword(password)
        val passwordRepeatErrorMsg: UiText? = UserValidator.validatePasswordRepeat(
            password = password,
            passwordRepeat = passwordRepeat,
        )
        val firstNameErrorMsg: UiText? = UserValidator.validateFirstName(firstName)
        val lastNameErrorMsg: UiText? = UserValidator.validateLastName(lastName)
        val checkboxErrorMsg: UiText? = UserValidator.validateCheckBox(isCheckBoxChecked)

        val errorMsgList = listOfNotNull(
            emailErrorMsg,
            passwordErrorMsg,
            passwordRepeatErrorMsg,
            firstNameErrorMsg,
            lastNameErrorMsg,
            checkboxErrorMsg,
        )

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

        return errorMsgList.isEmpty()
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

    private fun emailSignUp() {
        val email = emailState.text.toString().trim()
        val password = passwordState.text.toString()
        val firstName = firstNameState.text.toString().trim()
        val lastName = lastNameState.text.toString().trim()

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