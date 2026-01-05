package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.models.UiText
import org.koin.androidx.compose.koinViewModel

@Composable
fun MakeScreen(
    makeViewModel: MakeViewModel = koinViewModel(),
    navigateToWizard: () -> Unit,
){
    val state by makeViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        makeViewModel.effect.collect { effect ->
            when (effect) {
                MakeEffect.NavigateToWizard -> navigateToWizard()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ){
        item{
            SimpleServiceButton(
                text = UiText.StringResource(R.string.make_module_btn_text).asString(),
                isAnimated = false,
                onClick = { navigateToWizard() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}