package com.biernatmdev.simple_service

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeScreenContent
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeState
import org.junit.Rule
import org.junit.Test

class MakeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun makeScreen_displaysOffers_whenDataIsLoaded() {
        val testOffer = Offer(
            id = "1",
            title = "Testowa Oferta UI",
            price = 123.0,
            city = "Kraków",
            offerType = OfferType.PRODUCT,
            transactionType = TransactionType.OFFER,
            superCategory = OfferSuperCategory.ELECTRONICS,
            subcategory = OfferCategory.SMARTPHONES,
            authorId = "user1",
            createdAt = 0L,
            description = "Opis",
            images = emptyList(),
            currency = "PLN",
            priceUnit = OfferPriceUnit.ITEM,
            itemCondition = ItemCondition.NEW,
            status = OfferStatus.ACTIVE
        )

        val fakeState = MakeState(
            isLoading = false,
            displayingOffers = listOf(testOffer),
            offers = listOf(testOffer)
        )

        composeTestRule.setContent {
            MakeScreenContent(
                onEvent = {},
                state = fakeState,
                searchbarState = TextFieldState(),
                selectedPriceFrom = TextFieldState(),
                selectedPriceTo = TextFieldState(),
                cityState = TextFieldState(),
                navigateToWizard = {},
                snackbar = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithText("Testowa Oferta UI").assertIsDisplayed()
    }
}