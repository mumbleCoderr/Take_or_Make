package com.biernatmdev.simple_service.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val city: String? = null,
    val postalCode: Int? = null,
    val phoneNumber: PhoneNumber? = null,
)

@Serializable
data class PhoneNumber(
    val dialCode: Int,
    val number: String,
)