package com.biernatmdev.simple_service.core.offer.domain.repository

import com.biernatmdev.simple_service.core.offer.domain.model.Offer

interface OfferRepository {
    suspend fun createOffer(offer: Offer): Result<Unit>
    suspend fun updateOffer(offer: Offer): Result<Unit>
    suspend fun deleteOffer(offerId: String): Result<Unit>
    suspend fun getOffersByAuthorId(authorId: String, lastCreatedAt: Long?): Result<List<Offer>>
    suspend fun getAllOffers(lastCreatedAt: Long? = null): Result<List<Offer>>
    suspend fun toggleFavorite(offerId: String, isNowFavorite: Boolean): Result<Unit>
}