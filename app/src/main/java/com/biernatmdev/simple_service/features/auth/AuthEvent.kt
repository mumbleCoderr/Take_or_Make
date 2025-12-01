package com.biernatmdev.simple_service.features.auth

import com.google.firebase.auth.FirebaseUser

sealed interface AuthEvent {
    data object StartSignin : AuthEvent
    data class SetSigninSuccess(val user: FirebaseUser) : AuthEvent
    data class SetSigninFail(val message: String) : AuthEvent
    data object ClearError : AuthEvent
}

sealed interface AuthEffect {   
    data object NavigateToHome : AuthEffect
}