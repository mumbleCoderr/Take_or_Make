package com.biernatmdev.simple_service.core.user.data.repository

import com.biernatmdev.simple_service.core.user.data.mapper.toDomainUser
import com.biernatmdev.simple_service.core.user.data.mapper.toFirestoreMap
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.PhoneNumber
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.core.user.domain.model.UserException
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
            ?: throw UserException.NotSignedInException

        val document = Firebase.firestore
            .collection("user")
            .document(uid)
            .get()
            .await()

        if (!document.exists()) {
            throw UserException.NotFoundException
        }

        document.toDomainUser()
    }

    override suspend fun updateUserDetails(user: User): Result<Unit> = runCatching {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw UserException.NotSignedInException

        if (uid != user.id) {
            throw UserException.AccessDeniedException
        }

        val updatedUser = user.toFirestoreMap()

        Firebase.firestore.collection("user")
            .document(uid)
            .update(updatedUser)
            .await()
    }
}