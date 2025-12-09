package com.biernatmdev.simple_service.features.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.biernatmdev.simple_service.core.ui.model.IconType
import com.biernatmdev.simple_service.core.user.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navigateToAuth: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToAuth -> {
                    navigateToAuth()
                }

                is ProfileEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.LoadData)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.user == null){
            Icon(
                imageVector = Icons.Filled.Cancel,
                modifier = Modifier.size(128.dp),
                contentDescription = "ERROR"
            )
            Text("ERROR")
        }
    }
}

@Composable
fun UserProfilePictureSection(
    user: User
) {

}