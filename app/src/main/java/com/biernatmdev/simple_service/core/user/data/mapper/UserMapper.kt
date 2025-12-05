package com.biernatmdev.simple_service.core.user.data.mapper

import com.biernatmdev.simple_service.core.user.domain.model.PhoneNumber
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot


internal fun FirebaseUser.toDomainUser(): User{
    return User(
        id = this.uid,
        firstName = this.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
        lastName = this.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
        email = this.email ?: "Unknown",
        profilePicture = this.photoUrl.toString()
    )
}
internal fun DocumentSnapshot.toDomainUser(): User {
    val phoneNumberMap = this.get("phoneNumber") as? Map<*, *>
    val phoneNumber = phoneNumberMap?.let {
        val dialCode = (it["dialCode"] as? Long)?.toInt()
        val number = (it["number"] as? String)

        if (dialCode != null && number != null) {
            PhoneNumber(dialCode, number)
        } else null
    }

    return User(
        id = this.id,
        firstName = this.getString("firstName") ?: "Unknown",
        lastName = this.getString("lastName") ?: "Unknown",
        email = this.getString("email") ?: "Unknown",
        city = this.getString("city"),
        postalCode = this.getLong("postalCode")?.toInt(),
        phoneNumber = phoneNumber,
        address = this.getString("address"),
        addressAdditional = this.getString("addressAdditional"),
        profilePicture = this.getString("profilePicture")
    )
}

internal fun User.toFirestoreMap(): Map<String, Any?> {
    val phoneNumberMap = this.phoneNumber?.let {
        mapOf(
            "dialCode" to it.dialCode,
            "number" to it.number
        )
    }

    return mapOf(
        "firstName" to this.firstName,
        "lastName" to this.lastName,
        "email" to this.email,
        "address" to this.address,
        "addressAdditional" to this.addressAdditional,
        "city" to this.city,
        "postalCode" to this.postalCode,
        "phoneNumber" to phoneNumberMap,
        "profilePicture" to this.profilePicture
    )
}