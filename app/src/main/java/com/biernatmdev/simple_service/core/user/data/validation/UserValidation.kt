package com.biernatmdev.simple_service.core.user.data.validation

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText

object UserValidator {
    fun validateEmail(email: String): UiText? {
        if (email.isEmpty()) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_blank_error_msg)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_incorrect_error_msg)
        }
        if (email.length > 100)
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_email_too_long_error_msg)

        return null
    }

    fun validatePassword(password: String): UiText? {
        if (password.isEmpty()) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_blank_error_msg)
        }
        if (password.length < 6) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_too_short_error_msg)
        }
        if (password.length > 100)
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_too_short_error_msg)

        return null
    }

    fun validatePasswordRepeat(
        password: String,
        passwordRepeat: String,
    ): UiText? {
        if (passwordRepeat.isEmpty()) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_blank_error_msg)
        }
        if (password != passwordRepeat) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_password_not_identical_error_msg)
        }

        return null
    }

    fun validateFirstName(firstName: String): UiText? {
        if (firstName.isEmpty()) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_firstname_blank_error_msg)
        }
        if (firstName.any { !it.isLetter() && !it.isWhitespace() }) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_firstname_incorrect_error_msg)
        }
        if (firstName.length > 50) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_firstname_too_long_error_msg)
        }

        return null
    }

    fun validateLastName(lastName: String): UiText? {
        if (lastName.isEmpty()) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_lastname_blank_error_msg)
        }
        if (lastName.any { !it.isLetter() && !it.isWhitespace() && it != '-' && it != '\'' && it != '.' }) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_lastname_incorrect_error_msg)
        }
        if (lastName.length > 50) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_textfield_lastname_too_long_error_msg)
        }

        return null
    }

    fun validateCheckBox(isChecked: Boolean): UiText? {
        if (!isChecked) {
            return UiText.StringResource(R.string.auth_screen_bottom_section_checkbox_not_checked)
        }

        return null
    }

    fun validateAddress(address: String): UiText? {
        if (address.isBlank()){
            return UiText.StringResource(R.string.profile_details_screen_textfield_address_blank_error_msg)
        }
        if(address.length > 100){
            return UiText.StringResource(R.string.profile_details_screen_textfield_address_too_long_error_msg)
        }

        return null
    }

    fun validateAddressAdditional(addressAdditional: String): UiText? {
        if (addressAdditional.length > 50){
            return UiText.StringResource(R.string.profile_details_screen_textfield_address_additional_too_long_error_msg)
        }

        return null
    }

    fun validateCity(city: String): UiText? {
        if (city.isBlank()){
            return UiText.StringResource(R.string.profile_details_screen_city_blank_error_msg)
        }
        if(city.any { it.isDigit() }){
            return UiText.StringResource(R.string.profile_details_screen_city_incorrect_error_msg)
        }
        if(city.length > 100){
            return UiText.StringResource(R.string.profile_details_screen_city_too_long_error_msg)
        }

        return null
    }

    fun validatePostalCode(postalCode: String): UiText? {
        if (postalCode.isBlank()){
            return UiText.StringResource(R.string.profile_details_screen_postal_code_blank_error_msg)
        }
        if(!postalCode.isDigitsOnly()){
            return UiText.StringResource(R.string.profile_details_screen_postal_code_incorrect_error_msg)
        }
        if(postalCode.length > 50){
            return UiText.StringResource(R.string.profile_details_screen_postal_code_too_long_error_msg)
        }

        return null
    }

    fun validatePhoneNumber(phoneNumber: String): UiText? {
        if (phoneNumber.isBlank()) {
            return UiText.StringResource(R.string.profile_details_screen_phone_number_blank_error_msg)
        }

        if (!phoneNumber.startsWith("+")) {
            return UiText.StringResource(R.string.profile_details_screen_phone_number_incorrect_error_msg)
        }

        val isValidContent = phoneNumber.drop(1).all { it.isDigit() || it == ' ' }
        if (!isValidContent) {
            return UiText.StringResource(R.string.profile_details_screen_phone_number_incorrect_error_msg)
        }

        if (phoneNumber.length > 50) {
            return UiText.StringResource(R.string.profile_details_screen_phone_number_too_long_error_msg)
        }

        return null
    }

}