package com.biernatmdev.simple_service.features.offer_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.biernatmdev.simple_service.features.home.make_module.presentation.wizard.SummaryStepContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.BackFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.BuyOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.HandshakeOutlined
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun OfferDetailsScreen(
    offerDetailsViewModel: OfferDetailsViewModel = koinViewModel(),
    offer: Offer,
    navigateBack: () -> Unit,
    navigateToSuccessScreen: () -> Unit,
) {
    val state by offerDetailsViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        offerDetailsViewModel.effect.collect { effect ->
            when (effect) {
                OfferDetailsEffect.NavigateBack -> {
                    navigateBack()
                }
                is OfferDetailsEffect.ShowSnackbar -> {
                    snackbar.showSnackbar(effect.message.asString(context))
                }

                OfferDetailsEffect.NavigateToSuccessScreen -> {
                    navigateToSuccessScreen()
                }
            }
        }
    }

    OfferDetailsScreenContent(
        onEvent = offerDetailsViewModel::onEvent,
        state = state,
        offer = offer,
    )
}

@Composable
fun OfferDetailsScreenContent(
    onEvent: (OfferDetailsEvent) -> Unit,
    state: OfferDetailsState,
    offer: Offer,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val titleState = remember(offer) { TextFieldState(offer.title) }
        val priceState = remember(offer) { TextFieldState(offer.price.toString()) }
        val descriptionState = remember(offer) { TextFieldState(offer.description) }
        val cityState = remember(offer) { TextFieldState(offer.city) }

        val photosUris = remember(offer) {
            offer.images.map { it.toUri() }
        }
        TopNav(
            onBackClick = { onEvent(OfferDetailsEvent.OnBackClick) }
        )
        Spacer(Modifier.height(8.dp))
        SummaryStepContent(
            modifier = Modifier
                .padding(top = 16.dp)
                .weight(1f),
            selectedTransactionType = offer.transactionType,
            selectedOfferType = offer.offerType,
            titleState = titleState,
            priceState = priceState,
            selectedCurrency = offer.currency,
            selectedPriceUnit = offer.priceUnit,
            selectedSuperCategory = offer.superCategory,
            selectedCategory = offer.subcategory,
            descriptionState = descriptionState,
            cityState = cityState,
            selectedItemCondition = offer.itemCondition,
            selectedPhotos = photosUris,
            isReadOnly = true
        )
        Spacer(Modifier.height(24.dp))
        SimpleServiceButton(
            text = if(offer.offerType == OfferType.PRODUCT && offer.transactionType == TransactionType.OFFER){
                "Buy"
            } else if(offer.offerType == OfferType.PRODUCT && offer.transactionType == TransactionType.REQUEST) {
                "I have it"
            } else if(offer.offerType == OfferType.SERVICE && offer.transactionType == TransactionType.OFFER){
                "Order"
            }else{
                "I will do it"
            },
            isAnimated = true,
            isLoading = state.isLoading,
            icon = if(offer.offerType == OfferType.PRODUCT) IconType.Vector(BuyOutlined) else IconType.Vector(HandshakeOutlined),
            onClick = { onEvent(OfferDetailsEvent.OnBuyButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
fun TopNav(
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Icon(
            imageVector = BackFilled,
            tint = onColorBackground,
            contentDescription = stringResource(R.string.arrow_back),
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() }
                .align(Alignment.CenterStart)
        )
        Text(
            text = "Offer Details",
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}