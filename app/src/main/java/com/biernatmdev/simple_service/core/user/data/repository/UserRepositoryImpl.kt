package com.biernatmdev.simple_service.core.user.data.repository

import com.biernatmdev.simple_service.core.user.data.mapper.toDomainUser
import com.biernatmdev.simple_service.core.user.data.mapper.toFirestoreMap
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : UserRepository {
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

    override suspend fun linkGuestToEmail( //TODO IMPLEMENT IN PROFILE SCREEN
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<FirebaseUser> = runCatching {
        try {
            val currentUser = auth.currentUser ?: throw UserException.NotSignedInException
            val credential = EmailAuthProvider.getCredential(email, password)
            val result = currentUser.linkWithCredential(credential).await()
            val linkedUser = result.user ?: throw UserException.UnknownException

            val updates = mapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email
            )

            firestore.collection("user")
                .document(linkedUser.uid)
                .update(updates)
                .await()

            linkedUser
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthUserCollisionException -> UserException.EmailAlreadyInUse
                is FirebaseAuthWeakPasswordException -> UserException.WeakPassword
                is FirebaseAuthInvalidCredentialsException -> UserException.InvalidEmailFormat
                is FirebaseNetworkException -> UserException.NetworkError
                is FirebaseTooManyRequestsException -> UserException.TooManyRequests
                else -> e
            }
        }
    }

        override suspend fun signUpWithEmail(
            email: String,
            password: String,
            firstName: String,
            lastName: String
        ): Result<FirebaseUser> = runCatching {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user ?: throw UserException.UnknownException
                val newUser = User(
                    id = firebaseUser.uid,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                )

                firestore.collection("user")
                    .document(firebaseUser.uid)
                    .set(newUser.toFirestoreMap())
                    .await()

                firebaseUser
            } catch (e: Exception) {
                throw when (e) {
                    is FirebaseAuthWeakPasswordException -> UserException.WeakPassword
                    is FirebaseAuthInvalidCredentialsException -> UserException.InvalidEmailFormat
                    is FirebaseAuthUserCollisionException -> UserException.EmailAlreadyInUse
                    is FirebaseNetworkException -> UserException.NetworkError
                    is FirebaseTooManyRequestsException -> UserException.TooManyRequests
                    else -> e
                }
            }
        }

        override suspend fun signInWithEmail(
            email: String,
            password: String
        ): Result<FirebaseUser> = runCatching {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user ?: throw UserException.WrongCredentials
            } catch (e: Exception) {
                throw when (e) {
                    is FirebaseAuthInvalidUserException -> UserException.NotFoundException
                    is FirebaseAuthInvalidCredentialsException -> UserException.WrongCredentials
                    is FirebaseNetworkException -> UserException.NetworkError
                    is FirebaseTooManyRequestsException -> UserException.TooManyRequests
                    else -> e
                }
            }
        }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        try {
            auth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthInvalidUserException -> UserException.NotFoundException
                is FirebaseAuthInvalidCredentialsException -> UserException.InvalidEmailFormat
                is FirebaseNetworkException -> UserException.NetworkError
                is FirebaseTooManyRequestsException -> UserException.TooManyRequests
                else -> e
            }
        }
    }

    override suspend fun createUser(user: FirebaseUser): Result<Unit> = runCatching {
            try {
                val userCollection = Firebase.firestore.collection("user")
                val documentReference = userCollection.document(user.uid)
                val snapshot = documentReference.get().await()

                if (!snapshot.exists()) {
                    val newUser = user.toDomainUser()
                    documentReference.set(newUser).await()
                }
                Unit
            } catch (e: Exception) {
                throw if (e is FirebaseNetworkException) UserException.NetworkError else e
            }
        }

        override suspend fun signOut(): Result<Unit> = runCatching {
            stopObservingUser()
            Firebase.auth.signOut()
        }

        override suspend fun getUserDetails(): Result<User> = runCatching {
            try {
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
            } catch (e: Exception) {
                throw if (e is FirebaseNetworkException) UserException.NetworkError else e
            }
        }

        override suspend fun updateUserDetails(user: User): Result<Unit> = runCatching {
            try {
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
            } catch (e: Exception) {
                throw if (e is FirebaseNetworkException) UserException.NetworkError else e
            }
        }

    }