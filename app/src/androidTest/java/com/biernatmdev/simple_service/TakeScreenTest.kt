package com.biernatmdev.simple_service

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.features.home.take_module.TakeScreenContent
import com.biernatmdev.simple_service.features.home.take_module.TakeState
import org.junit.Rule
import org.junit.Test

class TakeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun takeScreen_displaysListOfOffers() {
        val testOffer = Offer(
            id = "take_1",
            title = "Paznokcie",
            price = 50.0,
            city = "Wrocław",
            offerType = OfferType.SERVICE,
            transactionType = TransactionType.OFFER,
            superCategory = OfferSuperCategory.BEAUTY_WELLNESS,
            subcategory = OfferCategory.NAILS,
            authorId = "teacher1",
            createdAt = 0L,
            description = "Zrobie pazy",
            images = emptyList(),
            currency = "PLN",
            priceUnit = OfferPriceUnit.HOUR,
            itemCondition = ItemCondition.NOT_APPLICABLE,
            status = OfferStatus.ACTIVE,
            isFavourite = false
        )

        val fakeState = TakeState(
            isLoading = false,
            displayingOffers = listOf(testOffer),
            offers = listOf(testOffer)
        )

        composeTestRule.setContent {
            TakeScreenContent(
                state = fakeState,
                onEvent = {},
                searchbarState = TextFieldState(),
                selectedPriceFrom = TextFieldState(),
                selectedPriceTo = TextFieldState(),
                cityState = TextFieldState(),
                snackbar = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithText("Paznokcie").assertIsDisplayed()
    }
}