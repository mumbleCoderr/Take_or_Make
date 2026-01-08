package com.biernatmdev.simple_service.core.offer.data.mapper

import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.google.firebase.firestore.DocumentSnapshot

internal fun Offer.toFirestoreMap(): Map<String, Any?> {
    return mapOf(
        "id" to this.id,
        "authorId" to this.authorId,
        "title" to this.title,
        "description" to this.description,
        "price" to this.price,
        "currency" to this.currency,
        "priceUnit" to this.priceUnit.name,
        "subcategory" to this.subcategory.name,
        "superCategory" to this.superCategory.name,
        "transactionType" to this.transactionType.name,
        "offerType" to this.offerType.name,
        "itemCondition" to this.itemCondition.name,
        "city" to this.city,
        "images" to this.images,
        "status" to this.status.name,
        "createdAt" to this.createdAt
    )
}

internal fun DocumentSnapshot.toDomainOffer(userFavoriteIds: List<String>): Offer {
    return Offer(
        id = this.id,
        authorId = this.getString("authorId") ?: "",
        title = this.getString("title") ?: "",
        description = this.getString("description") ?: "",

        price = this.getDouble("price") ?: 0.0,

        currency = this.getString("currency") ?: "",

        priceUnit = try {
            OfferPriceUnit.valueOf(this.getString("priceUnit") ?: "")
        } catch (e: Exception) { OfferPriceUnit.ITEM },

        superCategory = try {
            OfferSuperCategory.valueOf(this.getString("superCategory") ?: "")
        } catch (e: Exception) { OfferSuperCategory.OTHER_SERVICES },

        subcategory = try {
            OfferCategory.valueOf(this.getString("subcategory") ?: "")
        } catch (e: Exception) { OfferCategory.OTHER_SERVICE_ITEM },

        transactionType = try {
            TransactionType.valueOf(this.getString("transactionType") ?: "")
        } catch (e: Exception) { TransactionType.OFFER },

        offerType = try {
            OfferType.valueOf(this.getString("offerType") ?: "")
        } catch (e: Exception) { OfferType.SERVICE },

        itemCondition = try {
            ItemCondition.valueOf(this.getString("itemCondition") ?: "")
        } catch (e: Exception) { ItemCondition.NOT_APPLICABLE },

        city = this.getString("city") ?: "",

        images = (this.get("images") as? List<String>) ?: emptyList(),

        status = try {
            OfferStatus.valueOf(this.getString("status") ?: "")
        } catch (e: Exception) { OfferStatus.ACTIVE },

        createdAt = this.getLong("createdAt") ?: 0L,

        isFavourite = userFavoriteIds.contains(this.id)
    )
}