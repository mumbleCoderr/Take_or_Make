package com.biernatmdev.simple_service.core.offer.data.validation

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.ui.models.UiText

object OfferValidator {

    fun validateTitle(title: String): UiText? {
        if (title.isBlank()) {
            return UiText.StringResource(R.string.wizard_screen_textfield_title_blank_error_msg)
        }
        if (title.length > 40) {
            return UiText.StringResource(R.string.wizard_screen_textfield_title_too_long_error_msg)
        }

        return null
    }

    fun validatePrice(price: String): UiText? {
        if (price.isBlank()) {
            return UiText.StringResource(R.string.wizard_screen_textfield_price_blank_error_msg)
        }

        val priceRegex = Regex("^\\d+([.,]\\d{1,2})?$")

        if (!price.matches(priceRegex)) {
            return UiText.StringResource(R.string.wizard_screen_textfield_price_incorrect_error_msg)
        }

        val doubleValue = price.replace(',', '.').toDoubleOrNull()

        if (doubleValue == null) {
            return UiText.StringResource(R.string.wizard_screen_textfield_price_incorrect_error_msg)
        }

        if (doubleValue <= 0.0) {
            return UiText.StringResource(R.string.wizard_screen_textfield_price_too_low_error_msg)
        }

        if (doubleValue > 999999999.00) {
            return UiText.StringResource(R.string.wizard_screen_textfield_price_too_high_error_msg)
        }

        return null
    }

    fun validateTransactionTypePick(selectedTransactionType: TransactionType?): UiText? {
        if(selectedTransactionType == null){
            return UiText.StringResource(R.string.wizard_screen_pick_transaction_type_empty_error_msg)
        }

        return null
    }

    fun validateOfferTypePick(selectedOfferType: OfferType?): UiText? {
        if(selectedOfferType == null){
            return UiText.StringResource(R.string.wizard_screen_pick_offer_type_empty_error_msg)
        }

        return null
    }

    fun validateCategoryPick(selectedCategory: OfferCategory?): UiText? {
        if(selectedCategory == null){
            return UiText.StringResource(R.string.wizard_screen_pick_category_empty_error_msg)
        }

        return null
    }

    fun validateSuperCategoryPick(selectedSuperCategory: OfferSuperCategory?): UiText? {
        if(selectedSuperCategory == null){
            return UiText.StringResource(R.string.wizard_screen_pick_super_category_empty_error_msg)
        }

        return null
    }

    fun validateDescription(description: String): UiText? {
        if (description.length < 10) {
            return UiText.StringResource(R.string.wizard_screen_textfield_description_too_short_error_msg)
        }

        if (description.length > 1000) {
            return UiText.StringResource(R.string.wizard_screen_textfield_description_too_long_error_msg)
        }

        return null
    }

    fun validateCity(city: String): UiText? {
        if (city.isBlank()) {
            return UiText.StringResource(R.string.wizard_screen_textfield_city_blank_error_msg)
        }

        val cityRegex = Regex("^[\\p{L}\\s-]+$")

        if (!city.matches(cityRegex)) {
            return UiText.StringResource(R.string.wizard_screen_textfield_city_incorrect_error_msg)
        }

        if (city.length > 100) {
            return UiText.StringResource(R.string.wizard_screen_textfield_city_too_long_error_msg)
        }

        return null
    }
}