package com.biernatmdev.simple_service.core.offer.domain.utils

import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.components.filter.FilterState

fun List<Offer>.filterOffers(
    filters: FilterState,
    queryTitle: String,
    queryCity: String,
    priceFrom: Double?,
    priceTo: Double?
): List<Offer> {

    return this.filter { offer ->
        if (queryTitle.isNotEmpty()) {
            if (!offer.title.contains(queryTitle, ignoreCase = true)) return@filter false
        }

        if (filters.selectedTransactionType != null && offer.transactionType != filters.selectedTransactionType) {
            return@filter false
        }

        if (filters.selectedOfferType != null && offer.offerType != filters.selectedOfferType) {
            return@filter false
        }

        if (filters.selectedSubcategory != null && filters.selectedSubcategory != OfferCategory.ANY) {
            if (offer.subcategory != filters.selectedSubcategory) return@filter false
        } else if (filters.selectedSuperCategory != null && filters.selectedSuperCategory != OfferSuperCategory.ANY) {
            if (offer.superCategory != filters.selectedSuperCategory) return@filter false
        }

        if (filters.selectedCurrency != "ANY") {
            if (!offer.currency.equals(filters.selectedCurrency, ignoreCase = true)) return@filter false
        }

        if (filters.selectedPriceUnit != OfferPriceUnit.ANY) {
            if (offer.priceUnit != filters.selectedPriceUnit) return@filter false
        }

        if (priceFrom != null || priceTo != null) {
            val offerPrice = offer.price
            if (offerPrice == null) return@filter false

            if (priceFrom != null && offerPrice < priceFrom) return@filter false
            if (priceTo != null && offerPrice > priceTo) return@filter false
        }

        if (queryCity.isNotEmpty()) {
            if (!offer.city.contains(queryCity, ignoreCase = true)) return@filter false
        }

        if (filters.selectedItemCondition != null) {
            if (offer.itemCondition != filters.selectedItemCondition) return@filter false
        }

        true
    }
}