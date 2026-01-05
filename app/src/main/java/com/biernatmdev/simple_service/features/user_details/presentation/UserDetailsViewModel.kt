package com.biernatmdev.simple_service.features.user_details.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.data.validation.UserValidator
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.features.user_details.domain.UserDetailsFormItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(UserDetailsState())
    val state = _state.asStateFlow()

    private val _effect = Channel<UserDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    private val textFieldStates: Map<UserDetailsFormItem, TextFieldState> =
        UserDetailsFormItem.entries.associateWith { TextFieldState() }

    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
                    _state.update {
                        it.copy(
                            user = user,
                            profilePicture = user.profilePicture
                        )
                    }
                    populateTextFields(user)
                }
            }
        }
        viewModelScope.launch {
            combine(
                snapshotFlow {
                    listOf(
                        textFieldStates[UserDetailsFormItem.FIRSTNAME]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.LASTNAME]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.EMAIL]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.ADDRESS]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.ADDRESS_ADDITIONAL]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.CITY]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.POSTAL_CODE]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.PHONE_NUMBER]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.PASSWORD]?.text.toString(),
                        textFieldStates[UserDetailsFormItem.PASSWORD_REPEAT]?.text.toString()
                    )
                },
                _state.map { it.profilePicture }.distinctUntilChanged(),
                _state.map { it.user }.distinctUntilChanged()
            ) { textValues, currentPfp, user ->
                if (user == null) return@combine false

                val firstName = textValues[0].trim()
                val lastName = textValues[1].trim()
                val email = textValues[2].trim()
                val address = textValues[3].trim()
                val addressAdditional = textValues[4].trim()
                val city = textValues[5].trim()
                val postalCode = textValues[6].trim()
                val phoneNumber = textValues[7].trim()
                val password = textValues[8]
                val passwordRepeat = textValues[9]

                if (user.isGuest) {
                    password.isNotBlank() && passwordRepeat.isNotBlank()
                } else {
                    val isPfpChanged = currentPfp != user.profilePicture

                    val isBasicInfoChanged = firstName != user.firstName ||
                            lastName != user.lastName ||
                            email != user.email ||
                            address != user.address ||
                            addressAdditional != user.addressAdditional ||
                            city != user.city ||
                            postalCode != (user.postalCode?.toString() ?: "") ||
                            phoneNumber != user.phoneNumber

                    isPfpChanged || isBasicInfoChanged
                }

            }.collect { isModified ->
                _state.update { it.copy(isFormModified = isModified) }
            }
        }
    }


    fun onEvent(event: UserDetailsEvent) {
        when (event) {
            UserDetailsEvent.OnButtonBackClick -> sendEffect(UserDetailsEffect.NavigateBack)
            UserDetailsEvent.OnConfirmChangesButtonClick -> updateUserData()
            is UserDetailsEvent.OnNewPfpSelected -> {
                _state.update { it.copy(profilePicture = event.profilePicture) }
            }

            UserDetailsEvent.OnTextFieldCancelFocus -> {
                _state.update { it.copy(activeEditingItem = null) }
            }

            is UserDetailsEvent.OnUserDetailsFormItemClick -> {
                _state.update { it.copy(activeEditingItem = event.item) }
            }

            is UserDetailsEvent.OnTextFieldFocused -> {
                _state.update { it.copy(textFieldErrors = it.textFieldErrors - event.item) }
            }

            else -> Unit
        }
    }

    private fun sendEffect(effect: UserDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    fun getTextFieldState(item: UserDetailsFormItem): TextFieldState {
        return textFieldStates[item] ?: TextFieldState()
    }

    fun getTextFieldError(item: UserDetailsFormItem): UiText? {
        return _state.value.textFieldErrors[item]
    }

    private fun populateTextFields(user: User) {
        textFieldStates[UserDetailsFormItem.FIRSTNAME]?.edit { replace(0, length, user.firstName) }
        textFieldStates[UserDetailsFormItem.LASTNAME]?.edit { replace(0, length, user.lastName) }
        textFieldStates[UserDetailsFormItem.EMAIL]?.edit { replace(0, length, user.email) }
        textFieldStates[UserDetailsFormItem.ADDRESS]?.edit { replace(0, length, user.address) }
        textFieldStates[UserDetailsFormItem.ADDRESS_ADDITIONAL]?.edit { replace(0, length, user.addressAdditional) }
        textFieldStates[UserDetailsFormItem.CITY]?.edit { replace(0, length, user.city) }
        textFieldStates[UserDetailsFormItem.POSTAL_CODE]?.edit { replace(0, length, user.postalCode?.toString() ?: "") }
        textFieldStates[UserDetailsFormItem.PHONE_NUMBER]?.edit { replace(0, length, user.phoneNumber) }
    }

    private fun validateUpdateUserDetailsForm(): Boolean {
        val user = _state.value.user ?: return false
        val isGuest = user.isGuest

        val email = textFieldStates[UserDetailsFormItem.EMAIL]?.text.toString().trim()
        val firstName = textFieldStates[UserDetailsFormItem.FIRSTNAME]?.text.toString().trim()
        val lastName = textFieldStates[UserDetailsFormItem.LASTNAME]?.text.toString().trim()
        val address = textFieldStates[UserDetailsFormItem.ADDRESS]?.text.toString().trim()
        val addressAdditional = textFieldStates[UserDetailsFormItem.ADDRESS_ADDITIONAL]?.text.toString().trim()
        val city = textFieldStates[UserDetailsFormItem.CITY]?.text.toString().trim()
        val postalCode = textFieldStates[UserDetailsFormItem.POSTAL_CODE]?.text.toString().trim()
        val phoneNumber = textFieldStates[UserDetailsFormItem.PHONE_NUMBER]?.text.toString().trim()

        val password = textFieldStates[UserDetailsFormItem.PASSWORD]?.text.toString()
        val passwordRepeat = textFieldStates[UserDetailsFormItem.PASSWORD_REPEAT]?.text.toString()

        val emailErrorMsg = UserValidator.validateEmail(email)
        val firstNameErrorMsg = UserValidator.validateFirstName(firstName)
        val lastNameErrorMsg = UserValidator.validateLastName(lastName)
        val addressErrorMsg = UserValidator.validateAddress(address)
        val addressAdditionalErrorMsg = UserValidator.validateAddressAdditional(addressAdditional)
        val cityErrorMsg = UserValidator.validateCity(city)
        val postalCodeErrorMsg = UserValidator.validatePostalCode(postalCode)
        val phoneNumberErrorMsg = UserValidator.validatePhoneNumber(phoneNumber)

        val (passwordErrorMsg, passwordRepeatErrorMsg) = if (isGuest) {
            val passErr = UserValidator.validatePassword(password)
            val passRepeatErr = UserValidator.validatePasswordRepeat(password, passwordRepeat)
            Pair(passErr, passRepeatErr)
        } else {
            Pair(null, null)
        }

        val errorMsgMap = listOfNotNull(
            emailErrorMsg?.let { UserDetailsFormItem.EMAIL to it },
            firstNameErrorMsg?.let { UserDetailsFormItem.FIRSTNAME to it },
            lastNameErrorMsg?.let { UserDetailsFormItem.LASTNAME to it },
            addressErrorMsg?.let { UserDetailsFormItem.ADDRESS to it },
            addressAdditionalErrorMsg?.let { UserDetailsFormItem.ADDRESS_ADDITIONAL to it },
            cityErrorMsg?.let { UserDetailsFormItem.CITY to it },
            postalCodeErrorMsg?.let { UserDetailsFormItem.POSTAL_CODE to it },
            phoneNumberErrorMsg?.let { UserDetailsFormItem.PHONE_NUMBER to it },
            passwordErrorMsg?.let { UserDetailsFormItem.PASSWORD to it },
            passwordRepeatErrorMsg?.let { UserDetailsFormItem.PASSWORD_REPEAT to it }
        ).toMap()

        _state.update {
            it.copy(textFieldErrors = errorMsgMap)
        }

        return errorMsgMap.isEmpty()
    }

    private fun updateUserData() {
        if (validateUpdateUserDetailsForm()) {
            val currentUser = _state.value.user ?: return

            val firstName = textFieldStates[UserDetailsFormItem.FIRSTNAME]?.text.toString().trim()
            val lastName = textFieldStates[UserDetailsFormItem.LASTNAME]?.text.toString().trim()
            val email = textFieldStates[UserDetailsFormItem.EMAIL]?.text.toString().trim()
            val password = textFieldStates[UserDetailsFormItem.PASSWORD]?.text.toString()

            val address = textFieldStates[UserDetailsFormItem.ADDRESS]?.text.toString().trim()
            val addressAdditional = textFieldStates[UserDetailsFormItem.ADDRESS_ADDITIONAL]?.text.toString().trim()
            val city = textFieldStates[UserDetailsFormItem.CITY]?.text.toString().trim()
            val postalCode = textFieldStates[UserDetailsFormItem.POSTAL_CODE]?.text.toString().toIntOrNull()
            val phoneNumber = textFieldStates[UserDetailsFormItem.PHONE_NUMBER]?.text.toString().trim()

            val profilePicture = _state.value.profilePicture

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                if (currentUser.isGuest) {
                    userRepository.linkGuestToEmail(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName
                    ).onSuccess { linkedFirebaseUser ->
                        val fullUserUpdate = User(
                            id = linkedFirebaseUser.uid,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            address = address,
                            addressAdditional = addressAdditional,
                            city = city,
                            postalCode = postalCode,
                            phoneNumber = phoneNumber,
                            profilePicture = profilePicture
                        )

                        userRepository.updateUserDetails(fullUserUpdate)
                            .onSuccess {
                                _state.update { s -> s.copy(isLoading = false, user = fullUserUpdate) }
                                sendEffect(UserDetailsEffect.ShowSnackbar(UiText.StringResource(R.string.profile_details_screen_info_account_linked)))
                                sendEffect(UserDetailsEffect.NavigateBack)
                            }
                            .onFailure { e ->
                                _state.update { s -> s.copy(isLoading = false) }
                                handleException(e)
                            }

                    }.onFailure { e ->
                        _state.update { s -> s.copy(isLoading = false) }
                        handleException(e)
                    }

                } else {
                    val updatedUser = currentUser.copy(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        address = address,
                        addressAdditional = addressAdditional,
                        city = city,
                        postalCode = postalCode,
                        phoneNumber = phoneNumber,
                        profilePicture = profilePicture
                    )

                    userRepository.updateUserDetails(updatedUser)
                        .onSuccess {
                            _state.update { s -> s.copy(isLoading = false, user = updatedUser) }
                            sendEffect(UserDetailsEffect.ShowSnackbar(UiText.StringResource(R.string.profile_details_screen_info_account_details_changed)))
                            sendEffect(UserDetailsEffect.NavigateBack)
                        }
                        .onFailure { e ->
                            _state.update { s -> s.copy(isLoading = false) }
                            handleException(e)
                        }
                }
            }
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is UserException.NetworkError -> UiText.StringResource(R.string.snackbar_msg_info_auth_no_internet_connection)
            is UserException.NotSignedIn -> UiText.StringResource(R.string.snackbar_msg_info_auth_user_not_signed_in)
            is UserException.AccessDenied -> UiText.StringResource(R.string.snackbar_msg_info_auth_access_denied)
            else -> UiText.DynamicString(exception.message ?: "External server error")
        }
    }
}