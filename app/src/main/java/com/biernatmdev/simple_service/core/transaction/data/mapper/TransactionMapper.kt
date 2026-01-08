package com.biernatmdev.simple_service.core.transaction.data.mapper

import com.biernatmdev.simple_service.core.transaction.domain.model.Transaction
import com.google.firebase.firestore.DocumentSnapshot

internal fun Transaction.toFirestoreMap(): Map<String, Any?> {
    return mapOf(
        "id" to this.id,
        "userOrderingOfferId" to this.userOrderingOfferId,
        "userAuthoringOfferId" to this.userAuthoringOfferId,
        "offerId" to this.offerId,
        "createdAt" to this.createdAt
    )
}

internal fun DocumentSnapshot.toDomainTransaction(): Transaction {
    return Transaction(
        id = this.id,
        userOrderingOfferId = this.getString("userOrderingOfferId") ?: "",
        userAuthoringOfferId = this.getString("userAuthoringOfferId") ?: "",
        offerId = this.getString("offerId") ?: "",
        createdAt = this.getLong("createdAt") ?: 0L
    )
}