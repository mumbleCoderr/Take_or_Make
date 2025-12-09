package com.biernatmdev.simple_service.core.google_auth

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleUiClient(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val serverClientId: String,
) {
    private val credentialManager by lazy { CredentialManager.Companion.create(context) }

    suspend fun signInWithGoogle(activity: Activity): AuthResult {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(activity, request)

        val googleCredential = when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    GoogleIdTokenCredential.Companion.createFrom(credential.data)
                } else {
                    error("Unsupported credential type: ${credential.type}")
                }
            } else -> error("Unsupported credential class: ${result.credential::class.java.name}")
        }

        val idToken = googleCredential.idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        return auth.signInWithCredential(firebaseCredential).await()
    }

    suspend fun signInGuest(): AuthResult = auth.signInAnonymously().await()

    suspend fun signOut() {
        auth.signOut()
        runCatching {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }

    val currentUser get() = auth.currentUser

    suspend fun validateUserSession(): Boolean {
        val user = auth.currentUser ?: return false

        return try {
            user.reload().await()
            true
        } catch (e: Exception) {
            signOut()
            false
        }
    }
}