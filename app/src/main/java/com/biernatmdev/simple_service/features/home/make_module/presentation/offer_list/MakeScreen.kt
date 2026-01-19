package com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.ui.components.OfferCard
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceDialog
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceSnackbar
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceTextField
import com.biernatmdev.simple_service.core.ui.components.dropdowns.CategoryDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.CurrencyDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.ItemConditionDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.PriceUnitDropdown
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_SMALL
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CheckFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CloseFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.DeleteFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.EditFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Empty
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LocalisationOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Offering
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Product
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Requesting
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.SearchOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Service
import com.biernatmdev.simple_service.core.ui.theme.Resources.Image.AppForegroundImage
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.core.ui.components.filter.FilterEvent
import com.biernatmdev.simple_service.core.ui.components.filter.FilterState
import com.biernatmdev.simple_service.core.ui.components.filter.MainFilterDropdown
import com.biernatmdev.simple_service.core.ui.components.filter.SearchBarFilter
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeEffect.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun MakeScreen(
    // TODO INACTIVE OFFERS BELOW DIVIDER WITH ALFA 0.5 && BUTTON FOR DISABLE OFFER SO API CALLS AND CHA NGE IT IN FB
    makeViewModel: MakeViewModel = koinViewModel(),
    navigateToWizard: (Offer?) -> Unit,
) {
    val state by makeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        makeViewModel.effect.collect { effect ->
            when (effect) {
                is ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }

                is NavigateToWizard -> {
                    navigateToWizard(effect.offer)
                }
            }
        }
    }

    MakeScreenContent(
        onEvent = makeViewModel::onEvent,
        state = state,
        searchbarState = makeViewModel.searchbarState,
        selectedPriceFrom = makeViewModel.priceFromState,
        selectedPriceTo = makeViewModel.priceToState,
        cityState = makeViewModel.cityState,
        navigateToWizard = { navigateToWizard(it) },
        snackbar = snackbar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeScreenContent(
    onEvent: (MakeEvent) -> Unit,
    state: MakeState,
    searchbarState: TextFieldState,
    selectedPriceFrom: TextFieldState,
    selectedPriceTo: TextFieldState,
    cityState: TextFieldState,
    navigateToWizard: (Offer?) -> Unit,
    snackbar: SnackbarHostState,
) {
    val isAnyFilterActive =
        state.filterState.selectedTransactionType != null ||
                state.filterState.selectedOfferType != null ||
                state.filterState.selectedCurrency != "ANY" ||
                state.filterState.selectedPriceUnit != OfferPriceUnit.ANY ||
                state.filterState.selectedSuperCategory != null ||
                state.filterState.selectedSubcategory != null ||
                state.filterState.selectedItemCondition != null ||
                selectedPriceFrom.text.isNotEmpty() ||
                selectedPriceTo.text.isNotEmpty() ||
                cityState.text.isNotEmpty() ||
                searchbarState.text.isNotEmpty()

    val focusManager = LocalFocusManager.current

    if (state.isDeleteDialogVisible) {
        SimpleServiceDialog(
            onDismiss = { onEvent(MakeEvent.OnDeleteDialogDismiss) },
            onConfirm = { onEvent(MakeEvent.OnDeleteDialogConfirm) },
            title = UiText.StringResource(R.string.make_module_dialog_delete_title),
            subtext = UiText.StringResource(R.string.make_module_dialog_delete_subtext),
        )
    }

    PullToRefreshBox(
        isRefreshing = state.isPullRefreshing,
        onRefresh = { onEvent(MakeEvent.OnPullToRefresh) },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
    ) {
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
                        text = UiText.StringResource(R.string.make_module_title).asString(),
                        fontSize = SEMI_LARGE,
                        lineHeight = LineHeight.SEMI_LARGE,
                        color = onColorBackground,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = UiText.StringResource(R.string.make_module_subtitle).asString(),
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
                    onSearchbarButtonClick = { onEvent(MakeEvent.Filter(FilterEvent.OnSearchbarButtonClick)) }
                )
                Spacer(Modifier.height(16.dp))
                MainFilterDropdown(
                    filterState = state.filterState,

                    onFilterEvent = { filterEvent ->
                        onEvent(MakeEvent.Filter(filterEvent))
                    },

                    priceFromState = selectedPriceFrom,
                    priceToState = selectedPriceTo,
                    cityState = cityState,

                    animationDuration = 600,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
            }
            item {
                EmptyListComponent(
                    isOfferListEmpty = state.displayingOffers.isEmpty(),
                    isActiveFilter = isAnyFilterActive,
                    isLoading = state.isPullRefreshing || state.isLoadingNextPage,
                )
            }
            itemsIndexed(state.displayingOffers) { index, offer ->
                if (index >= state.displayingOffers.lastIndex && !state.isLoadingNextPage && !state.isPullRefreshing) {
                    LaunchedEffect(Unit) {
                        onEvent(MakeEvent.OnLoadNextPage)
                    }
                }
                OfferCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    offer = offer,
                    isPhotoLeading = state.offers.indexOf(offer) % 2 == 0,
                    onOfferCardClick = { onEvent(MakeEvent.OnOfferCardClick(offer)) }
                )
                Spacer(Modifier.height(32.dp))
            }
            if (state.isLoadingNextPage && state.displayingOffers.isNotEmpty()) {
                item {
                    Box(
                        Modifier
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
            item {
                Spacer(Modifier.height(32.dp))
                SimpleServiceButton(
                    text = UiText.StringResource(R.string.make_module_btn_text_add).asString(),
                    isAnimated = false,
                    onClick = { onEvent(MakeEvent.OnAddNewOfferButtonClick) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(64.dp))
            }
        }
        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.TopCenter),
            snackbar = { data ->
                SimpleServiceSnackbar(data)
            }
        )

        // --- POPUP ---

        val showPopup = state.isOfferCardPopUpIsVisible
        val offerToDisplay = state.selectedOffer

        AnimatedVisibility(
            visible = showPopup,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.zIndex(10f)
        ) {
            if (offerToDisplay != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable { onEvent(MakeEvent.OnOfferCardPopUpDismiss) },
                    contentAlignment = Alignment.Center
                ) {
                    OfferCardPopUp(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .animateEnterExit(
                                enter = scaleIn(tween(300), initialScale = 0.8f),
                                exit = scaleOut(tween(200), targetScale = 0.8f)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {},
                        title = offerToDisplay.title,
                        photo = offerToDisplay.images.firstOrNull(),
                        price = "${offerToDisplay.price ?: "-"}  ${offerToDisplay.currency}  /  ${offerToDisplay.priceUnit.displayName.asString()}",
                        transactionType = offerToDisplay.transactionType,
                        offerType = offerToDisplay.offerType,
                        status = offerToDisplay.status,
                        onEditClick = { onEvent(MakeEvent.OnEditOfferClick(offerToDisplay)) },
                        onDeleteClick = { onEvent(MakeEvent.OnDeleteOfferClick(offerToDisplay)) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyListComponent(
    isOfferListEmpty: Boolean,
    isActiveFilter: Boolean,
    isLoading: Boolean,
    isFavouriteScreen: Boolean = false,
    isNotificationScreenTmp: Boolean = false,
) {
    if (isOfferListEmpty && !isLoading) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = UiText.StringResource(R.string.empty_list_component_title).asString(),
                fontSize = SEMI_LARGE,
                lineHeight = LineHeight.SEMI_LARGE,
                color = onColorBackground,
                fontWeight = FontWeight.Bold,
                fontFamily = momoFont(),
            )
            Spacer(Modifier.height(16.dp))
            Icon(
                painter = painterResource(Empty),
                tint = ColorPrimary,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = if (isActiveFilter && !isFavouriteScreen && !isNotificationScreenTmp) {
                    UiText.StringResource(R.string.empty_list_component_filter).asString()
                } else if (!isActiveFilter && !isFavouriteScreen && !isNotificationScreenTmp) {
                    UiText.StringResource(R.string.empty_list_component_add_offer).asString()
                } else if (!isActiveFilter && !isFavouriteScreen) {
                    UiText.StringResource(R.string.empty_list_component_notification).asString()
                } else {
                    UiText.StringResource(R.string.empty_list_component_add_favourite).asString()
                },
                fontSize = SEMI_LARGE,
                lineHeight = LineHeight.SEMI_LARGE,
                color = onColorBackgroundDarker,
                fontWeight = FontWeight.Bold,
                fontFamily = momoFont(),
            )
        }
    }
}

@Composable
fun OfferCardPopUp(
    modifier: Modifier = Modifier,
    title: String,
    photo: String?,
    price: String,
    transactionType: TransactionType,
    offerType: OfferType,
    status: OfferStatus = OfferStatus.ACTIVE,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(22.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .fallback(AppForegroundImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
            ) {
                Text(
                    text = title,
                    color = onColorBackground,
                    fontFamily = momoFont(),
                    fontWeight = FontWeight.Bold,
                    fontSize = SEMI_LARGE,
                    maxLines = 2,
                    lineHeight = LineHeight.SEMI_LARGE,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 4.dp)
                )
                Text(
                    text = price,
                    color = onColorBackgroundDarker,
                    fontFamily = momoFont(),
                    fontWeight = FontWeight.Bold,
                    fontSize = MEDIUM,
                    maxLines = 2,
                    lineHeight = LineHeight.MEDIUM,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SimpleServiceButton(
                text = UiText.StringResource(R.string.make_module_btn_text_delete).asString(),
                isAnimated = false,
                onClick = { onDeleteClick() },
                textFontSize = MEDIUM,
                backgroundColor = ColorSecondary,
                textColor = onColorBackgroundDarker,
                icon = IconType.Vector(DeleteFilled),
                iconTint = onColorBackgroundDarker,
                iconSize = 24.dp,
                isIconLeading = true,
                buttonPadding = 16.dp,
                roundedCornerShapeValue = 14.dp,
                modifier = Modifier.weight(1f)
            )
            SimpleServiceButton(
                text = UiText.StringResource(R.string.make_module_btn_text_edit).asString(),
                isAnimated = false,
                onClick = { onEditClick() },
                textFontSize = MEDIUM,
                icon = IconType.Vector(EditFilled),
                isIconLeading = true,
                iconSize = 24.dp,
                buttonPadding = 16.dp,
                roundedCornerShapeValue = 14.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


