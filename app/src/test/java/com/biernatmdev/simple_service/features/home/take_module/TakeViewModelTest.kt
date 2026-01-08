package com.biernatmdev.simple_service.features.home.take_module

import com.biernatmdev.simple_service.MainDispatcherRule
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TakeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val offerRepository = mockk<OfferRepository>()
    private lateinit var viewModel: TakeViewModel

    @Test
    fun `OnFavouriteClick optimistically updates UI`() = runTest {
        // Given
        val offer = mockk<Offer>(relaxed = true) {
            coEvery { id } returns "1"
            coEvery { isFavourite } returns false
        }

        coEvery { offerRepository.getAllOffers(null) } returns Result.success(listOf(offer))
        coEvery { offerRepository.toggleFavorite(any(), any()) } returns Result.success(Unit)

        viewModel = TakeViewModel(offerRepository)
        viewModel.onEvent(TakeEvent.OnScreenRefresh)

        viewModel.onEvent(TakeEvent.OnFavouriteClick(offer))

        val updatedOffer = viewModel.state.value.offers.find { it.id == "1" }
        assertTrue(updatedOffer?.isFavourite == true)

        coVerify { offerRepository.toggleFavorite("1", true) }
    }

    @Test
    fun `OnFavouriteClick rolls back on network error`() = runTest {
        // Given
        val offer = mockk<Offer>(relaxed = true) {
            coEvery { id } returns "1"
            coEvery { isFavourite } returns false
        }

        coEvery { offerRepository.getAllOffers(null) } returns Result.success(listOf(offer))
        coEvery { offerRepository.toggleFavorite(any(), any()) } returns Result.failure(OfferException.NetworkError)

        viewModel = TakeViewModel(offerRepository)
        viewModel.onEvent(TakeEvent.OnScreenRefresh)

        viewModel.onEvent(TakeEvent.OnFavouriteClick(offer))

        val updatedOffer = viewModel.state.value.offers.find { it.id == "1" }
        assertFalse(updatedOffer?.isFavourite == true)
    }
}