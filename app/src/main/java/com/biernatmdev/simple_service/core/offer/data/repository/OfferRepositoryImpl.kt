package com.biernatmdev.simple_service.core.offer.data.repository

import com.biernatmdev.simple_service.core.offer.data.mapper.toDomainOffer
import com.biernatmdev.simple_service.core.offer.data.mapper.toFirestoreMap
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.ui.components.filter.FilterState
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class OfferRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : OfferRepository {
    private val userCollection = firestore.collection("user")
    private val collection = firestore.collection("offers")

    override suspend fun createOffer(offer: Offer): Result<Unit> = runCatching {

        try {
            val currentUser = auth.currentUser ?: throw UserException.NotSignedIn

            if(currentUser.isAnonymous){
                throw UserException.NotSignedIn
            }

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

            if(currentUser.isAnonymous){
                throw UserException.NotSignedIn
            }

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

    override suspend fun deleteOffer(offerId: String): Result<Unit> = runCatching {
        try {
            val currentUser = auth.currentUser ?: throw UserException.NotSignedIn

            if(currentUser.isAnonymous){
                throw UserException.NotSignedIn
            }

            collection.document(offerId)
                .delete()
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

    override suspend fun getOffersByAuthorId(authorId: String, lastCreatedAt: Long?): Result<List<Offer>> = runCatching {
        try {
            val favoriteIds = getFavoriteIds()

            var query = collection
                .whereEqualTo("authorId", authorId)
                .whereEqualTo("status", "ACTIVE")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(100)

            if (lastCreatedAt != null) {
                query = query.startAfter(lastCreatedAt)
            }

            val snapshot = query.get().await()

            snapshot.documents.map { doc -> doc.toDomainOffer(favoriteIds) }

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> OfferException.NetworkError
                else -> e
            }
        }
    }

    override suspend fun getAllOffers(lastCreatedAt: Long?): Result<List<Offer>> = runCatching {
        try {
            val favoriteIds = getFavoriteIds()

            var query = collection
                .whereEqualTo("status", "ACTIVE")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(100)

            if (lastCreatedAt != null) {
                query = query.startAfter(lastCreatedAt)
            }

            val snapshot = query.get().await()
            snapshot.documents.map { doc -> doc.toDomainOffer(favoriteIds) }

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> OfferException.NetworkError
                else -> e
            }
        }
    }

    override suspend fun getInactiveOffersByAuthorId(authorId: String, lastCreatedAt: Long?): Result<List<Offer>> = runCatching {
        try {
            val favoriteIds = getFavoriteIds()

            var query = collection
                .whereEqualTo("authorId", authorId)
                .whereEqualTo("status", "INACTIVE")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(100)

            if (lastCreatedAt != null) {
                query = query.startAfter(lastCreatedAt)
            }

            val snapshot = query.get().await()

            snapshot.documents.map { doc -> doc.toDomainOffer(favoriteIds) }

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> OfferException.NetworkError
                else -> e
            }
        }
    }

    override suspend fun getFavoriteOffers(lastCreatedAt: Long?): Result<List<Offer>> = runCatching {
        try {
            val favoriteIds = getFavoriteIds()

            if (favoriteIds.isEmpty()) {
                return@runCatching emptyList()
            }

            val offers = coroutineScope {
                favoriteIds.chunked(10).map { chunk ->
                    async {
                        try {
                            val snapshot = collection
                                .whereIn(FieldPath.documentId(), chunk)
                                .get()
                                .await()
                            snapshot.documents.map { doc -> doc.toDomainOffer(favoriteIds) }
                        } catch (e: Exception) {
                            emptyList<Offer>()
                        }
                    }
                }.awaitAll().flatten()
            }

            val sortedOffers = offers
                .filter { it.status == OfferStatus.ACTIVE }
                .sortedByDescending { it.createdAt }

            if (lastCreatedAt == null) {
                sortedOffers.take(100)
            } else {
                sortedOffers
                    .filter { it.createdAt < lastCreatedAt }
                    .take(100)
            }

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseNetworkException -> OfferException.NetworkError
                is FirebaseFirestoreException -> OfferException.NetworkError
                else -> e
            }
        }
    }

    override suspend fun toggleFavorite(offerId: String, isNowFavorite: Boolean): Result<Unit> = runCatching {
        val currentUser = auth.currentUser ?: throw UserException.NotSignedIn
        val userDocRef = userCollection.document(currentUser.uid)

        if (isNowFavorite) {
            userDocRef.update("favoriteOfferIds", FieldValue.arrayUnion(offerId)).await()
        } else {
            userDocRef.update("favoriteOfferIds", FieldValue.arrayRemove(offerId)).await()
        }
        Unit
    }

    private suspend fun getFavoriteIds(): List<String> {
        val currentUser = auth.currentUser ?: return emptyList()
        return try {
            val snapshot = userCollection.document(currentUser.uid).get().await()
            (snapshot.get("favoriteOfferIds") as? List<String>) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}