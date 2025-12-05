package com.biernatmdev.simple_service.core.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String? = null,
    val addressAdditional: String? = null,
    val city: String? = null,
    val postalCode: Int? = null,
    val phoneNumber: PhoneNumber? = null,
    val profilePicture: String? = null
)

@Serializable
data class PhoneNumber(
    val dialCode: Int,
    val number: String,
)