package com.biernatmdev.simple_service.core.user.data.repository

import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.PhoneNumber
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    override fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun createUser(user: FirebaseUser): Result<Unit> = runCatching {
        val userCollection = Firebase.firestore.collection("user")
        val documentReference = userCollection.document(user.uid)
        val snapshot = documentReference.get().await()

        if (!snapshot.exists()) {
            val user = User(
                id = user.uid,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                email = user.email ?: "Unknown",
                profilePicture = user.photoUrl.toString(),
            )
            documentReference.set(user).await()
        }
        Unit
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        Firebase.auth.signOut()
    }

    override suspend fun getUserDetails(): Result<User> = runCatching {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("User not logged in")

        val document = Firebase.firestore
            .collection("user")
            .document(uid)
            .get()
            .await()

        if (!document.exists()) {
            throw Exception("User does not exist")
        }

        val phoneNumberMap = document.get("phoneNumber") as? Map<*, *>
        val phoneNumber = phoneNumberMap?.let {
            val dialCode = (it["dialCode"] as? Long)?.toInt()
            val number = (it["number"] as? String)

            if(dialCode != null && number  != null){
                PhoneNumber(
                    dialCode = dialCode,
                    number = number
                )
            } else null
        }

        User(
            id = document.id,
            firstName = document.getString("firstName") ?: "Unknown",
            lastName = document.getString("lastName") ?: "Unknown",
            email = document.getString("email") ?: "Unknown",
            city = document.getString("city"),
            postalCode = document.getLong("postalCode")?.toInt(),
            phoneNumber = phoneNumber,
            address = document.getString("address"),
            addressAdditional = document.getString("addressAdditional"),
            profilePicture = document.getString("profilePicture")
        )

    }
}