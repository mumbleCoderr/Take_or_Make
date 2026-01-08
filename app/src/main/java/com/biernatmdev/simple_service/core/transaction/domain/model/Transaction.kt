package com.biernatmdev.simple_service.core.transaction.domain.model

data class Transaction(
    val id: String,
    val userOrderingOfferId: String,
    val userAuthoringOfferId: String,
    val offerId: String,
    val createdAt: Long
)