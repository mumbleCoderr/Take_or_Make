package com.biernatmdev.simple_service.features.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.features.home.components.TopBar
import com.biernatmdev.simple_service.features.home.domain.HomeMode
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = koinViewModel()
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
                        Text("TAKE")
                    }
                    HomeMode.MAKE -> {
                        Text("MAKE")
                    }
                }
            }

        }
    }

}