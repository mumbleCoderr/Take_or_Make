package com.biernatmdev.simple_service.core.offer.domain.model

import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType

data class Offer(
    val id: String = "",
    val authorId: String = "",
    val transactionType: TransactionType = TransactionType.OFFER,
    val offerType: OfferType = OfferType.SERVICE,
    val category: OfferCategory = OfferCategory.OTHER_SERVICE_ITEM,
    val priceUnit: OfferPriceUnit = OfferPriceUnit.PROJECT,
    val itemCondition: ItemCondition = ItemCondition.NOT_APPLICABLE,
    val status: OfferStatus = OfferStatus.ACTIVE,
    val title: String = "",
    val description: String = "",
    val price: Double? = null,
    val currency: String = "",
    val images: List<String> = emptyList(),
    val city: String = "",
    val createdAt: Long = System.currentTimeMillis()
)