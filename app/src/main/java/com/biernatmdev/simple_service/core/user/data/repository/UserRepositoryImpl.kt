package com.biernatmdev.simple_service.core.user.data.repository

import com.biernatmdev.simple_service.core.user.data.mapper.toDomainUser
import com.biernatmdev.simple_service.core.user.data.mapper.toFirestoreMap
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.PhoneNumber
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    override fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun createUser(user: FirebaseUser): Result<Unit> = runCatching {
        val userCollection = Firebase.firestore.collection("user")
        val documentReference = userCollection.document(user.uid)
        val snapshot = documentReference.get().await()

        if (!snapshot.exists()) {
            val newUser = user.toDomainUser()
            documentReference.set(newUser).await()
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

        document.toDomainUser()
    }

    override suspend fun updateUserDetails(user: User): Result<Unit> = runCatching {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("User not logged in")

        if (uid != user.id) {
            throw Exception("Can not update other user's data")
        }

        val updatedUser = user.toFirestoreMap()

        Firebase.firestore.collection("user")
            .document(uid)
            .update(updatedUser)
            .await()
    }
}