package com.biernatmdev.simple_service.core.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val address: String = "",
    val addressAdditional: String = "",
    val city: String = "",
    val postalCode: Int? = null,
    val phoneNumber: String = "",
    val profilePicture: String = "",
    val favoriteOfferIds: List<String> = emptyList(),
){
    val isGuest: Boolean
        get() = email.isBlank()
}
