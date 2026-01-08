package com.biernatmdev.simple_service.core.transaction.data.repository

import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.transaction.data.mapper.toFirestoreMap
import com.biernatmdev.simple_service.core.transaction.domain.model.Transaction
import com.biernatmdev.simple_service.core.transaction.domain.repository.TransactionRepository
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class TransactionRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TransactionRepository {

    private val collection = firestore.collection("transactions")

    override suspend fun createTransaction(transaction: Transaction): Result<Unit> = runCatching {
        try {
            if (auth.currentUser == null) {
                throw UserException.NotSignedIn
            }

            val newDocRef = collection.document()

            val finalTransaction = transaction.copy(
                id = newDocRef.id,
                createdAt = if (transaction.createdAt == 0L) System.currentTimeMillis() else transaction.createdAt
            )

            newDocRef.set(finalTransaction.toFirestoreMap()).await()

            Unit
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> OfferException.NetworkError
                else -> e
            }
        }
    }
}