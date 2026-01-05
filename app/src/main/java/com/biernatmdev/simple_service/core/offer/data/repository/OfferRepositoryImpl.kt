package com.biernatmdev.simple_service.core.offer.data.repository

import com.biernatmdev.simple_service.core.offer.data.mapper.toFirestoreMap
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class OfferRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : OfferRepository {

    private val collection = firestore.collection("offers")

    override suspend fun createOffer(offer: Offer): Result<Unit> = runCatching {
        try {
            val currentUser = auth.currentUser ?: throw UserException.NotSignedIn

            val newDocRef = collection.document()

            val finalOffer = offer.copy(
                id = newDocRef.id,
                authorId = currentUser.uid,
                createdAt = System.currentTimeMillis()
            )

            newDocRef.set(finalOffer.toFirestoreMap()).await()

            Unit
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> OfferException.NetworkError
                else -> e
            }
        }
    }

    override suspend fun updateOffer(offer: Offer): Result<Unit> = runCatching {
        try {
            val currentUser = auth.currentUser ?: throw UserException.NotSignedIn

            if (offer.authorId != currentUser.uid) {
                throw OfferException.AccessDenied
            }

            collection.document(offer.id)
                .set(offer.toFirestoreMap())
                .await()

            Unit
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> {
                    when (e.code) {
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> OfferException.AccessDenied
                        FirebaseFirestoreException.Code.NOT_FOUND -> OfferException.NotFound
                        else -> OfferException.UnknownException
                    }
                }
                else -> e
            }
        }
    }
}