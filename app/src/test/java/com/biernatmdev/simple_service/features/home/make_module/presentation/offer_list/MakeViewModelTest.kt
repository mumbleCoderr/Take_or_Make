package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import com.biernatmdev.simple_service.MainDispatcherRule
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
    fun `OnDeleteDialogConfirm calls delete repository and updates state`() = runTest {
        val offerId = "offer_to_delete"
        val offer = mockk<Offer>(relaxed = true) {
            every { id } returns offerId
        }

        coEvery { offerRepository.deleteOffer(offerId) } returns Result.success(Unit)
        coEvery { userRepository.getCurrentUserId() } returns "user1"

        viewModel = MakeViewModel(offerRepository, userRepository)

        viewModel.onEvent(MakeEvent.OnDeleteOfferClick(offer))

        assertTrue(viewModel.state.value.isDeleteDialogVisible)

        viewModel.onEvent(MakeEvent.OnDeleteDialogConfirm)

        coVerify(exactly = 1) { offerRepository.deleteOffer(offerId) }

        val state = viewModel.state.value
        assertFalse(state.isDeleteDialogVisible)

        assertNull(state.selectedOffer)
    }

    @Test
    fun `OnDeleteDialogConfirm shows error when repository fails`() = runTest {
        val offerId = "offer_fail"
        val offer = mockk<Offer>(relaxed = true) { every { id } returns offerId }

        coEvery { offerRepository.deleteOffer(offerId) } returns Result.failure(Exception("DB Error"))
        coEvery { userRepository.getCurrentUserId() } returns "user1"

        viewModel = MakeViewModel(offerRepository, userRepository)

        viewModel.onEvent(MakeEvent.OnDeleteOfferClick(offer))

        viewModel.onEvent(MakeEvent.OnDeleteDialogConfirm)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.error != null)
    }
}