package com.biernatmdev.simple_service.core.user.data.repository

import com.biernatmdev.simple_service.core.user.domain.UserRepository
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

        if(!snapshot.exists()){
            val user = User(
                id = user.uid,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                email = user.email ?: "Unknown",
            )
            documentReference.set(user).await()
        }
        Unit
    }

    override suspend fun signOut(): Result<Unit> = runCatching{
        Firebase.auth.signOut()
    }
}