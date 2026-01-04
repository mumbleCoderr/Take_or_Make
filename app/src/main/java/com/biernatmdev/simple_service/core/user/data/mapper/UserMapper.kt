package com.biernatmdev.simple_service.core.user.data.mapper

import com.biernatmdev.simple_service.core.user.domain.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

internal fun FirebaseUser.toDomainUser(): User{
    return User(
        id = this.uid,
        firstName = this.displayName?.split(" ")?.firstOrNull() ?: "",
        lastName = this.displayName?.split(" ")?.lastOrNull() ?: "",
        email = this.email ?: "",
        profilePicture = this.photoUrl?.toString() ?: "",
        phoneNumber = this.phoneNumber ?: ""
    )
}

internal fun DocumentSnapshot.toDomainUser(): User {
    return User(
        id = this.id,
        firstName = this.getString("firstName") ?: "",
        lastName = this.getString("lastName") ?: "",
        email = this.getString("email") ?: "",
        city = this.getString("city") ?: "",
        postalCode = this.getLong("postalCode")?.toInt(),
        phoneNumber = this.getString("phoneNumber") ?: "",
        address = this.getString("address") ?: "",
        addressAdditional = this.getString("addressAdditional") ?: "",
        profilePicture = this.getString("profilePicture") ?: "",
        favoriteOfferIds = (this.get("favoriteOfferIds") as? List<String>) ?: emptyList()
    )
}

internal fun User.toFirestoreMap(): Map<String, Any?> {
    return mapOf(
        "id" to this.id,
        "firstName" to this.firstName,
        "lastName" to this.lastName,
        "email" to this.email,
        "address" to this.address,
        "addressAdditional" to this.addressAdditional,
        "city" to this.city,
        "postalCode" to this.postalCode,
        "phoneNumber" to this.phoneNumber,
        "profilePicture" to this.profilePicture,
        "favoriteOfferIds" to this.favoriteOfferIds,
    )
}