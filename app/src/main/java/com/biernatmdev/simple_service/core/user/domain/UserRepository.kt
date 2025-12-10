package com.biernatmdev.simple_service.core.user.domain

import com.biernatmdev.simple_service.core.user.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    fun getCurrentUserId(): String?
    suspend fun createUser(user: FirebaseUser): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun getUserDetails(): Result<User>
    suspend fun updateUserDetails(user: User): Result<Unit>
    val currentUser: StateFlow<User?>
    fun startObservingUser(uid: String)
    fun stopObservingUser()
}
