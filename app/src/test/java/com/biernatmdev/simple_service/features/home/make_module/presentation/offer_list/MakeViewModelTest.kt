package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import com.biernatmdev.simple_service.MainDispatcherRule
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MakeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val offerRepository = mockk<OfferRepository>()
    private val userRepository = mockk<UserRepository>()
    private lateinit var viewModel: MakeViewModel

    @Test
    fun `OnScreenRefresh fetches offers successfully`() = runTest {
        val userId = "user123"
        val offers = listOf(mockk<Offer>())

        coEvery { userRepository.getCurrentUserId() } returns userId
        coEvery { offerRepository.getOffersByAuthorId(userId, null) } returns Result.success(offers)

        viewModel = MakeViewModel(offerRepository, userRepository)

        viewModel.onEvent(MakeEvent.OnScreenRefresh)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(offers, state.offers)
    }

    @Test
    fun `OnScreenRefresh shows error when not logged in`() = runTest {
        coEvery { userRepository.getCurrentUserId() } returns null

        viewModel = MakeViewModel(offerRepository, userRepository)

        viewModel.onEvent(MakeEvent.OnScreenRefresh)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.error != null)
    }
}