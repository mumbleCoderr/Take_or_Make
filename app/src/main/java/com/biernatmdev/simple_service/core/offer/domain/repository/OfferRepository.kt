package com.biernatmdev.simple_service.core.offer.domain.repository

import com.biernatmdev.simple_service.core.offer.domain.model.Offer

interface OfferRepository {
    suspend fun createOffer(offer: Offer): Result<Unit>
    suspend fun updateOffer(offer: Offer): Result<Unit>
}