package com.biernatmdev.simple_service.features.user_details.domain

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.model.User

enum class UserDetailsFormItem(
    val title: UiText,
    val getValue: (User) -> String?
) {
    FIRSTNAME(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_firstname),
        getValue = { user -> user.firstName },
    ),
    LASTNAME(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_lastname),
        getValue = { user -> user.lastName },
    ),
    EMAIL(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_email),
        getValue = { user -> user.email },
    ),
    ADDRESS(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_address),
        getValue = { user -> user.address },
    ),
    ADDRESS_ADDITIONAL(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_address_additional),
        getValue = { user -> user.addressAdditional },
    ),
    CITY(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_city),
        getValue = { user -> user.city },
    ),
    POSTAL_CODE(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_postal_code),
        getValue = { user -> user.postalCode.toString() },
    ),
    PHONE_NUMBER(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_phone),
        getValue = { user -> user.phoneNumber },
    ),
    PASSWORD(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_password),
        getValue = { user -> "" }
    ),
    PASSWORD_REPEAT(
        title = UiText.StringResource(R.string.profile_details_screen_form_item_title_password_repeat),
        getValue = { user -> "" }
    ),
}