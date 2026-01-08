package com.biernatmdev.simple_service.core.offer.domain.utils

import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.components.filter.FilterState
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterOffersTest {

    private val sampleOffer = Offer(
        id = "1",
        title = "Detailing samochodu",
        price = 600.0,
        city = "Warszawa",
        offerType = OfferType.SERVICE,
        transactionType = TransactionType.OFFER,
        superCategory = OfferSuperCategory.AUTO_SERVICES,
        subcategory = OfferCategory.AUTO_DETAILING,
        authorId = "user1",
        createdAt = 0L,
        description = "",
        images = emptyList(),
        currency = "PLN",
        priceUnit = OfferPriceUnit.ITEM,
        itemCondition = ItemCondition.USED,
        status = OfferStatus.ACTIVE
    )

    private val offerList = listOf(sampleOffer)

    @Test
    fun `filterOffers returns offer when criteria match`() {
        val filters = FilterState(
            selectedOfferType = OfferType.SERVICE,
            selectedTransactionType = TransactionType.OFFER
        )
        val result = offerList.filterOffers(
            filters = filters,
            queryTitle = "Detailing",
            queryCity = "Warszawa",
            priceFrom = 100.0,
            priceTo = 1000.0
        )

        assertEquals(1, result.size)
        assertEquals(sampleOffer, result[0])
    }

    @Test
    fun `filterOffers excludes offer when price is too high`() {
        val filters = FilterState()

        val result = offerList.filterOffers(
            filters = filters,
            queryTitle = "",
            queryCity = "",
            priceFrom = null,
            priceTo = 500.0
        )

        assertEquals(0, result.size)
    }
}