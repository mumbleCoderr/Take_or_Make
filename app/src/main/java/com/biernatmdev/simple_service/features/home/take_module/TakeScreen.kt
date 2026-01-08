package com.biernatmdev.simple_service.features.home.take_module

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.core.nav.Screen
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.components.OfferCard
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceSnackbar
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEvent
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.EmptyListComponent
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MainFilterDropdown
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.SearchBarFilter
import org.koin.androidx.compose.koinViewModel

@Composable
fun TakeScreen(
    takeViewModel: TakeViewModel = koinViewModel(),
    navigateToOfferDetails: (Offer) -> Unit,
){
    val state by takeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        takeViewModel.effect.collect { effect ->
            when(effect){
                is TakeEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }

                is TakeEffect.NavigateToOfferDetails -> {
                    navigateToOfferDetails(effect.offer)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        takeViewModel.onEvent(TakeEvent.OnScreenRefresh)
    }

    TakeScreenContent(
        state = state,
        onEvent = takeViewModel::onEvent,
        searchbarState = takeViewModel.searchbarState,
        selectedPriceFrom = takeViewModel.priceFromState,
        selectedPriceTo = takeViewModel.priceToState,
        cityState = takeViewModel.cityState,
        snackbar = snackbar,
    )
}

@Composable
fun TakeScreenContent(
    state: TakeState,
    onEvent: (TakeEvent) -> Unit,
    searchbarState: TextFieldState,
    selectedPriceFrom: TextFieldState,
    selectedPriceTo: TextFieldState,
    cityState: TextFieldState,
    snackbar: SnackbarHostState,
){
    val focusManager = LocalFocusManager.current
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { onEvent(TakeEvent.OnPullToRefresh) },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.height(36.dp))
                    Text(
                        text = "Newest offers",
                        fontSize = SEMI_LARGE,
                        lineHeight = LineHeight.SEMI_LARGE,
                        color = onColorBackground,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Feel free to order something!",
                        fontSize = SEMI_LARGE,
                        lineHeight = LineHeight.SEMI_LARGE,
                        color = onColorBackgroundDarker,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }
            item {
                SearchBarFilter(
                    searchbarState = searchbarState,
                    onSearchbarButtonClick = { onEvent(TakeEvent.Filter(FilterEvent.OnSearchbarButtonClick)) }
                )
                Spacer(Modifier.height(16.dp))
                MainFilterDropdown(
                    filterState = state.filterState,

                    onFilterEvent = { filterEvent ->
                        onEvent(TakeEvent.Filter(filterEvent))
                    },

                    priceFromState = selectedPriceFrom,
                    priceToState = selectedPriceTo,
                    cityState = cityState,

                    animationDuration = 600,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
            }
            item{
                EmptyListComponent(
                    isOfferListEmpty = state.displayingOffers.isEmpty(),
                    isActiveFilter = true,
                    isLoading = state.isLoading,
                )
            }
            itemsIndexed(state.displayingOffers) { index, offer ->
                if (index >= state.displayingOffers.lastIndex && !state.isLoading) {
                    LaunchedEffect(Unit) {
                        onEvent(TakeEvent.OnLoadNextPage)
                    }
                }
                OfferCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    isPhotoLeading = state.offers.indexOf(offer) % 2 == 0,
                    isTakeModule = true,
                    onFavouriteClick = { onEvent(TakeEvent.OnFavouriteClick(offer)) },
                    onOfferCardClick = { onEvent(TakeEvent.OnOfferClick(offer)) },
                    offer = offer,
                )
                Spacer(Modifier.height(32.dp))
            }
            if (state.isLoading && state.displayingOffers.isNotEmpty()) {
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

