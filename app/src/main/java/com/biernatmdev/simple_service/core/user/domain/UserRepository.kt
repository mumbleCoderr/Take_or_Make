package com.biernatmdev.simple_service.core.user.domain

import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun getCurrentUserId(): String?
    suspend fun createUser(user: FirebaseUser): Result<Unit>
}