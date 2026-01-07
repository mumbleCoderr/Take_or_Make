package com.biernatmdev.simple_service.features.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.features.home.components.TopBar
import com.biernatmdev.simple_service.features.home.domain.HomeMode
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeScreen
import com.biernatmdev.simple_service.features.home.take_module.TakeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToWizard: (Offer?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        TopBar(
            selectedMode = state.mode,
            onModeSelect = { selection ->
                viewModel.onEvent(HomeEvent.ChangeMode(selection))
            }
        )
        AnimatedContent(
            targetState = state.mode,
            label = "home_content",
            modifier = Modifier.weight(1f)
        ) { mode ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when(mode) {
                    HomeMode.TAKE -> {
                        TakeScreen()
                    }
                    HomeMode.MAKE -> {
                        MakeScreen(
                            navigateToWizard = { navigateToWizard(it) }
                        )
                    }
                }
            }

        }
    }

}