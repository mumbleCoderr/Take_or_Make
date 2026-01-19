package com.biernatmdev.simple_service.features.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.components.OfferCard
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceSnackbar
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.EmptyListComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesScreen(
    favouritesViewModel: FavouritesViewModel = koinViewModel(),
    navigateToOfferDetails: (Offer) -> Unit,
) {
    val state by favouritesViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        favouritesViewModel.effect.collect { effect ->
            when (effect) {
                is FavouritesEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }

                is FavouritesEffect.NavigateToOfferDetails -> {
                    navigateToOfferDetails(effect.offer)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        favouritesViewModel.onEvent(FavouritesEvent.OnScreenRefresh)
    }

    FavouritesScreenContent(
        state = state,
        onEvent = favouritesViewModel::onEvent,
        snackbar = snackbar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreenContent(
    state: FavouritesState,
    onEvent: (FavouritesEvent) -> Unit,
    snackbar: SnackbarHostState,
) {
    PullToRefreshBox(
        isRefreshing = state.isPullRefreshing,
        onRefresh = { onEvent(FavouritesEvent.OnPullToRefresh) },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.height(36.dp))
                    Text(
                        text = UiText.StringResource(R.string.favourites_screen_title).asString(),
                        fontSize = SEMI_LARGE,
                        lineHeight = LineHeight.SEMI_LARGE,
                        color = onColorBackground,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = UiText.StringResource(R.string.favourites_screen_subtitle).asString(),
                        fontSize = SEMI_LARGE,
                        lineHeight = LineHeight.SEMI_LARGE,
                        color = onColorBackgroundDarker,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                    )
                    Spacer(Modifier.height(48.dp))
                }
            }

            item {
                EmptyListComponent(
                    isOfferListEmpty = state.offers.isEmpty(),
                    isActiveFilter = false,
                    isLoading = state.isPullRefreshing,
                    isFavouriteScreen = true,
                )
            }

            itemsIndexed(state.offers) { index, offer ->
                if (index >= state.offers.lastIndex && !state.isLoadingNextPage && !state.isPullRefreshing) {
                    LaunchedEffect(Unit) {
                        onEvent(FavouritesEvent.OnLoadNextPage)
                    }
                }

                OfferCard(
                    modifier = Modifier.fillMaxWidth(),
                    isPhotoLeading = index % 2 == 0,
                    isTakeModule = true,
                    offer = offer,
                    onFavouriteClick = { onEvent(FavouritesEvent.OnFavoriteClick(offer)) },
                    onOfferCardClick = { onEvent(FavouritesEvent.OnOfferClick(offer)) },
                )
                Spacer(Modifier.height(32.dp))
            }

            if (state.isLoadingNextPage && state.offers.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = ColorPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.TopCenter),
            snackbar = { data ->
                SimpleServiceSnackbar(data)
            }
        )
    }
}