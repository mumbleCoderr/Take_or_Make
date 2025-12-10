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
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    override fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    private var listenerRegistration: ListenerRegistration? = null

    override fun startObservingUser(uid: String) {
        if (listenerRegistration != null) return

        listenerRegistration = Firebase.firestore.collection("user").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        val user = snapshot.toDomainUser()
                        _currentUser.value = user
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    _currentUser.value = null
                }
            }
    }

    override fun stopObservingUser() {
        listenerRegistration?.remove()
        listenerRegistration = null
        _currentUser.value = null
    }

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
        stopObservingUser()
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