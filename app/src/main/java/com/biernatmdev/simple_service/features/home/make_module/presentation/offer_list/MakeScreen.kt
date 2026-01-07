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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_SMALL
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
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
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeEffect.*
import com.biernatmdev.simple_service.features.profile.presentation.ProfileEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun MakeScreen(
    makeViewModel: MakeViewModel = koinViewModel(),
    navigateToWizard: (Offer?) -> Unit,
) {
    val state by makeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        makeViewModel.effect.collect { effect ->
            when (effect) {
                is MakeEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }

                is MakeEffect.NavigateToWizard -> {
                    navigateToWizard(effect.offer)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        makeViewModel.onEvent(MakeEvent.OnScreenRefresh)
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
        isRefreshing = state.isLoading,
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
            /*if (state.offers.isEmpty() && !state.isLoading && state.error == null) {
                item {
                    Text("No offers yet. Add one!", modifier = Modifier.padding(16.dp))
                }
            }*/
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.height(36.dp))
                    Text(
                        text = "Your offers",
                        fontSize = SEMI_LARGE,
                        lineHeight = LineHeight.SEMI_LARGE,
                        color = onColorBackground,
                        fontWeight = FontWeight.Bold,
                        fontFamily = momoFont(),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Here you can search, modify and remove",
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
                MainFilterDropdown(
                    animationDuration = 600,

                    isMainFilterExpanded = state.isFilterDropdownExpanded,
                    onDropdownClick = { onEvent(MakeEvent.OnMainFilterDropdownClick) },

                    onSearchbarButtonClick = { onEvent(MakeEvent.OnSearchbarButtonClick) },
                    searchbarState = searchbarState,

                    selectedTransactionType = state.selectedTransactionType,
                    onTransactionTypeFilterDropdownClick = { onEvent(MakeEvent.OnTransactionTypeFilterDropdownClick) },
                    onTransactionTypeFilterClick = { onEvent(MakeEvent.OnTransactionTypeClick(it)) },
                    isTransactionTypeFilterDropdownExpanded = state.isTransactionTypeFilterDropdownExpanded,

                    selectedOfferType = state.selectedOfferType,
                    onOfferTypeFilterDropdownClick = { onEvent(MakeEvent.OnOfferTypeFilterDropdownClick) },
                    onOfferTypeFilterClick = { onEvent(MakeEvent.OnOfferTypeClick(it)) },
                    isOfferTypeFilterDropdownExpanded = state.isOfferTypeFilterDropdownExpanded,

                    onPriceFilterDropdownClick = { onEvent(MakeEvent.OnPriceFilterDropdownClick) },
                    isPriceFilterExpanded = state.isPriceDropdownExpanded,

                    selectedPriceFrom = selectedPriceFrom,
                    selectedPriceTo = selectedPriceTo,

                    selectedPriceCurrency = state.selectedCurrency,
                    isCurrencyDropdownExpanded = state.isCurrencyDropdownExpanded,
                    onCurrencyDropdownClick = { onEvent(MakeEvent.OnPriceCurrencyFilterDropdownClick) },
                    onCurrencySelected = { onEvent(MakeEvent.OnPriceCurrencyClick(it)) },
                    onCurrencyDropdownDismiss = { onEvent(MakeEvent.OnPriceCurrencyFilterDropdownDismiss) },

                    selectedPriceUnit = state.selectedPriceUnit,
                    isPriceUnitDropdownExpanded = state.isPriceUnitDropdownExpanded,
                    onPriceUnitDropdownClick = { onEvent(MakeEvent.OnPriceUnitFilterDropdownClick) },
                    onPriceUnitSelected = { onEvent(MakeEvent.OnPriceUnitClick(it)) },
                    onPriceUnitDropdownDismiss = { onEvent(MakeEvent.OnPriceUnitFilterDropdownDismiss) },

                    isCategoryFilterDropdownExpanded = state.isCategoryDropdownExpanded,
                    onCategoryFilterDropdownClick = { onEvent(MakeEvent.OnCategoryFilterDropdownClick) },

                    selectedSuperCategory = state.selectedSuperCategory,
                    isSuperCategoryFilterDropdownExpanded = state.isSuperCategoryDropdownExpanded,
                    onSuperCategoryFilterDropdownClick = { onEvent(MakeEvent.OnSuperCategoryFilterDropdownClick) },
                    onSuperCategoryFilterClick = { onEvent(MakeEvent.OnSuperCategoryClick(it)) },
                    onSuperCategoryFilterDropdownDismiss = { onEvent(MakeEvent.OnSuperCategoryFilterDropdownDismiss) },
                    onDisabledSuperCategoryClick = { onEvent(MakeEvent.OnDisabledSuperCategoryClick) },

                    selectedSubcategory = state.selectedSubcategory,
                    isSubcategoryFilterDropdownExpanded = state.isSubcategoryDropdownExpanded,
                    onSubcategoryFilterDropdownClick = { onEvent(MakeEvent.OnSubcategoryFilterDropdownClick) },
                    onSubcategoryFilterClick = { onEvent(MakeEvent.OnSubcategoryClick(it)) },
                    onSubcategoryFilterDropdownDismiss = { onEvent(MakeEvent.OnSubcategoryFilterDropdownDismiss) },
                    onDisabledSubcategoryClick = { onEvent(MakeEvent.OnDisabledSubcategoryClick) },

                    cityState = cityState,
                    onDisabledCityClick = { onEvent(MakeEvent.OnDisabledCityClick) },

                    selectedItemCondition = state.selectedItemCondition,
                    isItemConditionFilterDropdownExpanded = state.isItemConditionFilterDropdownExpanded,
                    onItemConditionFilterDropdownClick = { onEvent(MakeEvent.OnItemConditionFilterDropdownClick) },
                    onItemConditionFilterClick = { onEvent(MakeEvent.OnItemConditionFilterClick(it)) },
                    onItemConditionFilterDropdownDismiss = { onEvent(MakeEvent.OnItemConditionFilterDropdownDismiss) },
                    onDisabledItemConditionClick = { onEvent(MakeEvent.OnDisabledItemConditionClick) },

                    onApplyFiltersButtonClick = { onEvent(MakeEvent.OnApplyFiltersButtonClick) },
                    onClearFiltersButtonClick = { onEvent(MakeEvent.OnClearFiltersButtonClick) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
            }
            items(state.displayingOffers) { offer ->
                OfferCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    isPhotoLeading = state.offers.indexOf(offer) % 2 == 0,
                    title = offer.title,
                    photo = offer.images.firstOrNull(),
                    price = "${offer.price ?: "-"} ${offer.currency} / ${offer.priceUnit.displayName.asString()}",
                    transactionType = offer.transactionType,
                    offerType = offer.offerType,
                    status = offer.status,
                    onOfferCardClick = { onEvent(MakeEvent.OnOfferCardClick(offer)) }
                )
                Spacer(Modifier.height(32.dp))
            }
            item {
                Spacer(Modifier.height(32.dp))
                SimpleServiceButton(
                    text = UiText.StringResource(R.string.make_module_btn_text_add).asString(),
                    isAnimated = false,
                    onClick = { navigateToWizard(null) },
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
fun OfferCard(
    modifier: Modifier = Modifier,
    isPhotoLeading: Boolean,
    title: String,
    photo: String?,
    price: String,
    transactionType: TransactionType,
    offerType: OfferType,
    status: OfferStatus = OfferStatus.ACTIVE,
    onOfferCardClick: () -> Unit,
) {
    @Composable
    fun imageSection(modifier: Modifier) {
        Box(modifier = modifier) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .fallback(AppForegroundImage)
                    .crossfade(true)
                    .build(),
                contentDescription = UiText.StringResource(R.string.offer_photo).asString(),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    fun detailsSection(modifier: Modifier) {
        Column(
            modifier = modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = REGULAR,
                lineHeight = LineHeight.REGULAR,
                color = onColorBackground,
                fontWeight = FontWeight.Bold,
                fontFamily = momoFont(),
                maxLines = 2,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = if (transactionType == TransactionType.OFFER) painterResource(Offering) else painterResource(
                        Requesting
                    ),
                    tint = Color.Unspecified,
                    contentDescription = if (transactionType == TransactionType.OFFER) stringResource(
                        R.string.offering
                    ) else stringResource(R.string.requesting),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(16.dp))
                Icon(
                    painter = if (offerType == OfferType.PRODUCT) painterResource(Product) else painterResource(
                        Service
                    ),
                    tint = Color.Unspecified,
                    contentDescription = if (offerType == OfferType.PRODUCT) stringResource(R.string.product) else stringResource(
                        R.string.service
                    ),
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = price,
                fontSize = REGULAR,
                lineHeight = LineHeight.REGULAR,
                color = onColorBackgroundDarker,
                fontWeight = FontWeight.Bold,
                fontFamily = momoFont(),
            )
        }
    }

    Surface(
        color = ColorSecondary,
        shape = RoundedCornerShape(22.dp),
        modifier = modifier
            .height(140.dp)
            .clickable{ onOfferCardClick() },
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageSectionModifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f)
                val detailsSectionModifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f)

                if (isPhotoLeading) {
                    imageSection(imageSectionModifier)
                    detailsSection(detailsSectionModifier)
                } else {
                    detailsSection(detailsSectionModifier)
                    imageSection(imageSectionModifier)
                }
            }
            if (status == OfferStatus.INACTIVE) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }
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
){
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
                text = "Delete",
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
                text = "Edit",
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

@Composable
fun DropdownExpandAnimation(
    animationDuration: Int,
    isExpanded: Boolean,
    insideDropdown: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(
            animationSpec = tween(animationDuration),
            expandFrom = Alignment.Top
        ) + fadeIn(animationSpec = tween(animationDuration)),

        exit = shrinkVertically(
            animationSpec = tween(animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = tween(animationDuration))
    ) {
        Column {
            insideDropdown()
        }
    }
}

@Composable
fun MainFilterDropdown(
    animationDuration: Int,
    isMainFilterExpanded: Boolean,
    onDropdownClick: () -> Unit,

    onSearchbarButtonClick: () -> Unit,
    searchbarState: TextFieldState,

    selectedTransactionType: TransactionType?,
    onTransactionTypeFilterDropdownClick: () -> Unit,
    onTransactionTypeFilterClick: (TransactionType?) -> Unit,
    isTransactionTypeFilterDropdownExpanded: Boolean,

    selectedOfferType: OfferType?,
    onOfferTypeFilterDropdownClick: () -> Unit,
    onOfferTypeFilterClick: (OfferType?) -> Unit,
    isOfferTypeFilterDropdownExpanded: Boolean,

    onPriceFilterDropdownClick: () -> Unit,
    isPriceFilterExpanded: Boolean,

    selectedPriceFrom: TextFieldState,
    selectedPriceTo: TextFieldState,

    selectedPriceCurrency: String,
    isCurrencyDropdownExpanded: Boolean,
    onCurrencyDropdownClick: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    onCurrencyDropdownDismiss: () -> Unit,

    selectedPriceUnit: OfferPriceUnit,
    isPriceUnitDropdownExpanded: Boolean,
    onPriceUnitDropdownClick: () -> Unit,
    onPriceUnitSelected: (OfferPriceUnit) -> Unit,
    onPriceUnitDropdownDismiss: () -> Unit,

    isCategoryFilterDropdownExpanded: Boolean,
    onCategoryFilterDropdownClick: () -> Unit,

    selectedSuperCategory: OfferSuperCategory?,
    isSuperCategoryFilterDropdownExpanded: Boolean,
    onSuperCategoryFilterDropdownClick: () -> Unit,
    onSuperCategoryFilterClick: (OfferSuperCategory) -> Unit,
    onSuperCategoryFilterDropdownDismiss: () -> Unit,
    onDisabledSuperCategoryClick: () -> Unit,

    selectedSubcategory: OfferCategory?,
    isSubcategoryFilterDropdownExpanded: Boolean,
    onSubcategoryFilterDropdownClick: () -> Unit,
    onSubcategoryFilterClick: (OfferCategory) -> Unit,
    onSubcategoryFilterDropdownDismiss: () -> Unit,
    onDisabledSubcategoryClick: () -> Unit,

    cityState: TextFieldState,
    onDisabledCityClick: () -> Unit,

    selectedItemCondition: ItemCondition?,
    isItemConditionFilterDropdownExpanded: Boolean,
    onItemConditionFilterDropdownClick: () -> Unit,
    onItemConditionFilterClick: (ItemCondition) -> Unit,
    onItemConditionFilterDropdownDismiss: () -> Unit,
    onDisabledItemConditionClick: () -> Unit,

    onApplyFiltersButtonClick: () -> Unit,
    onClearFiltersButtonClick: () -> Unit,

    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilterDropdown(
            label = "Filters",
            isExpanded = isMainFilterExpanded,
            contentColor = onColorBackground,
            onDropdownClick = { onDropdownClick() },
        )
        Spacer(Modifier.height(16.dp))
        DropdownExpandAnimation(
            animationDuration = animationDuration,
            isExpanded = isMainFilterExpanded,
        ) {
            SearchBarFilter(
                searchbarState = searchbarState,
                onSearchbarButtonClick = { onSearchbarButtonClick() }
            )
            Spacer(Modifier.height(16.dp))
            TransactionAndOfferTypeFilter(
                animationDuration = animationDuration,

                selectedTransactionType = selectedTransactionType,
                onTransactionTypeFilterDropdownClick = { onTransactionTypeFilterDropdownClick() },
                onTransactionTypeFilterClick = { onTransactionTypeFilterClick(it) },
                isTransactionTypeFilterDropdownExpanded = isTransactionTypeFilterDropdownExpanded,

                selectedOfferType = selectedOfferType,
                onOfferTypeFilterDropdownClick = { onOfferTypeFilterDropdownClick() },
                onOfferTypeFilterClick = { onOfferTypeFilterClick(it) },
                isOfferTypeFilterDropdownExpanded = isOfferTypeFilterDropdownExpanded,
            )
            Spacer(Modifier.height(16.dp))
            CategoryFilter(
                animationDuration = animationDuration,
                isCategoryFilterDropdownExpanded = isCategoryFilterDropdownExpanded,
                onCategoryFilterDropdownClick = onCategoryFilterDropdownClick,

                selectedSuperCategory = selectedSuperCategory,
                isSuperCategoryFilterDropdownExpanded = isSuperCategoryFilterDropdownExpanded,
                onSuperCategoryFilterDropdownClick = { onSuperCategoryFilterDropdownClick() },
                onSuperCategoryFilterClick = { onSuperCategoryFilterClick(it) },
                onSuperCategoryFilterDropdownDismiss = { onSuperCategoryFilterDropdownDismiss() },
                onDisabledSuperCategoryClick = { onDisabledSuperCategoryClick() },

                selectedSubcategory = selectedSubcategory,
                isSubcategoryFilterDropdownExpanded = isSubcategoryFilterDropdownExpanded,
                onSubcategoryFilterDropdownClick = { onSubcategoryFilterDropdownClick() },
                onSubcategoryFilterClick = { onSubcategoryFilterClick(it) },
                onSubcategoryFilterDropdownDismiss = { onSubcategoryFilterDropdownDismiss() },
                onDisabledSubcategoryClick = { onDisabledSubcategoryClick() },

                selectedOfferType = selectedOfferType,
            )
            Spacer(Modifier.height(16.dp))
            PriceFilter(
                animationDuration = animationDuration,

                onPriceFilterDropdownClick = { onPriceFilterDropdownClick() },
                isPriceFilterExpanded = isPriceFilterExpanded,

                selectedPriceFrom = selectedPriceFrom,
                selectedPriceTo = selectedPriceTo,

                selectedPriceCurrency = selectedPriceCurrency,
                isCurrencyDropdownExpanded = isCurrencyDropdownExpanded,
                onCurrencyDropdownClick = { onCurrencyDropdownClick() },
                onCurrencySelected = { onCurrencySelected(it) },
                onCurrencyDropdownDismiss = { onCurrencyDropdownDismiss() },

                selectedPriceUnit = selectedPriceUnit,
                isPriceUnitDropdownExpanded = isPriceUnitDropdownExpanded,
                onPriceUnitDropdownClick = { onPriceUnitDropdownClick() },
                onPriceUnitSelected = { onPriceUnitSelected(it) },
                onPriceUnitDropdownDismiss = { onPriceUnitDropdownDismiss() }
            )
            Spacer(Modifier.height(16.dp))
            CityAndItemCondition(
                selectedOfferType = selectedOfferType,

                cityState = cityState,
                onDisabledCityClick = { onDisabledCityClick() },

                selectedItemCondition = selectedItemCondition,
                isItemConditionFilterDropdownExpanded = isItemConditionFilterDropdownExpanded,
                onItemConditionFilterDropdownClick = { onItemConditionFilterDropdownClick() },
                onItemConditionFilterClick = { onItemConditionFilterClick(it) },
                onItemConditionFilterDropdownDismiss = { onItemConditionFilterDropdownDismiss() },
                onDisabledItemConditionClick = { onDisabledItemConditionClick() },
            )
            Spacer(Modifier.height(16.dp))
            FilterButtonsSection(
                onApplyFiltersButtonClick = { onApplyFiltersButtonClick() },
                onClearFiltersButtonClick = { onClearFiltersButtonClick() }
            )
            Spacer(Modifier.height(120.dp))
        }
    }
}

@Composable
fun SearchBarFilter(
    searchbarState: TextFieldState,
    onSearchbarButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            SimpleServiceTextField(
                state = searchbarState,
                onFocus = {},
                errorText = null,
                verticalPadding = 8.dp,
                horizontalPadding = 16.dp,
                roundedCornerShapeValue = 14.dp,
                placeholder = "Search by title",
                icon = IconType.Vector(SearchOutlined),
            )
        }
        Spacer(Modifier.width(16.dp))
        SimpleServiceButton(
            text = "",
            buttonPadding = 8.dp,
            roundedCornerShapeValue = 14.dp,
            icon = IconType.Vector(SearchOutlined),
            iconSize = 24.dp,
            isAnimated = false,
            onClick = { onSearchbarButtonClick() }
        )
    }
}

@Composable
fun TransactionAndOfferTypeFilter(
    animationDuration: Int,

    selectedTransactionType: TransactionType?,
    onTransactionTypeFilterDropdownClick: () -> Unit,
    onTransactionTypeFilterClick: (TransactionType?) -> Unit,
    isTransactionTypeFilterDropdownExpanded: Boolean,

    selectedOfferType: OfferType?,
    onOfferTypeFilterDropdownClick: () -> Unit,
    onOfferTypeFilterClick: (OfferType?) -> Unit,
    isOfferTypeFilterDropdownExpanded: Boolean,
) {

    @Composable
    fun TransactionAndOfferTypeChip(
        onClick: () -> Unit,
        label: String,
        isSelected: Boolean,
        painter: Painter,
        modifier: Modifier = Modifier,
    ) {
        Surface(
            color = ColorSecondary,
            shape = RoundedCornerShape(14.dp),
            modifier = modifier
                .clickable { onClick() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painter,
                    tint = Color.Unspecified,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = label,
                    fontFamily = momoFont(),
                    color = if (isSelected) ColorPrimary else onColorBackgroundDarker,
                    fontSize = EXTRA_SMALL,
                )
            }

        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FilterDropdown(
                label = selectedTransactionType?.displayName?.asString() ?: "Transaction",
                isExpanded = isTransactionTypeFilterDropdownExpanded,
                contentColor = onColorBackgroundDarker,
                onDropdownClick = { onTransactionTypeFilterDropdownClick() }
            )
            DropdownExpandAnimation(
                animationDuration = animationDuration,
                isExpanded = isTransactionTypeFilterDropdownExpanded
            ) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Offering),
                        isSelected = selectedTransactionType == TransactionType.OFFER,
                        onClick = {
                            val next =
                                if (selectedTransactionType == TransactionType.OFFER) null else TransactionType.OFFER
                            onTransactionTypeFilterClick(next)
                        },
                        label = TransactionType.OFFER.displayName.asString(),
                        modifier = Modifier.weight(1f)
                    )
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Requesting),
                        isSelected = selectedTransactionType == TransactionType.REQUEST,
                        onClick = {
                            val next =
                                if (selectedTransactionType == TransactionType.REQUEST) null else TransactionType.REQUEST
                            onTransactionTypeFilterClick(next)
                        },
                        label = TransactionType.REQUEST.displayName.asString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FilterDropdown(
                label = selectedOfferType?.displayName?.asString() ?: "Offer",
                isExpanded = isOfferTypeFilterDropdownExpanded,
                contentColor = onColorBackgroundDarker,
                onDropdownClick = { onOfferTypeFilterDropdownClick() }
            )
            DropdownExpandAnimation(
                animationDuration = animationDuration,
                isExpanded = isOfferTypeFilterDropdownExpanded
            ) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Product),
                        isSelected = selectedOfferType == OfferType.PRODUCT,
                        onClick = {
                            val next =
                                if (selectedOfferType == OfferType.PRODUCT) null else OfferType.PRODUCT
                            onOfferTypeFilterClick(next)
                        },
                        label = OfferType.PRODUCT.displayName.asString(),
                        modifier = Modifier.weight(1f),
                    )
                    TransactionAndOfferTypeChip(
                        painter = painterResource(Service),
                        isSelected = selectedOfferType == OfferType.SERVICE,
                        onClick = {
                            val next =
                                if (selectedOfferType == OfferType.SERVICE) null else OfferType.SERVICE
                            onOfferTypeFilterClick(next)
                        },
                        label = OfferType.SERVICE.displayName.asString(),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
fun PriceFilter(
    animationDuration: Int,

    onPriceFilterDropdownClick: () -> Unit,
    isPriceFilterExpanded: Boolean,
    selectedPriceFrom: TextFieldState,
    selectedPriceTo: TextFieldState,

    selectedPriceCurrency: String,
    isCurrencyDropdownExpanded: Boolean,
    onCurrencyDropdownClick: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    onCurrencyDropdownDismiss: () -> Unit,

    selectedPriceUnit: OfferPriceUnit,
    isPriceUnitDropdownExpanded: Boolean,
    onPriceUnitDropdownClick: () -> Unit,
    onPriceUnitSelected: (OfferPriceUnit) -> Unit,
    onPriceUnitDropdownDismiss: () -> Unit,
) {
    FilterDropdown(
        label = "Price",
        isExpanded = isPriceFilterExpanded,
        contentColor = onColorBackgroundDarker,
        onDropdownClick = { onPriceFilterDropdownClick() }
    )
    DropdownExpandAnimation(
        animationDuration = animationDuration,
        isExpanded = isPriceFilterExpanded
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleServiceTextField(
                state = selectedPriceFrom,
                onFocus = {},
                isPhoneNumber = true,
                errorText = null,
                placeholder = "From",
                modifier = Modifier.weight(2.25f),
                verticalPadding = 8.dp,
                horizontalPadding = 8.dp,
                roundedCornerShapeValue = 14.dp,
            )
            SimpleServiceTextField(
                state = selectedPriceTo,
                onFocus = {},
                isPhoneNumber = true,
                errorText = null,
                placeholder = "To",
                modifier = Modifier.weight(2.25f),
                verticalPadding = 8.dp,
                horizontalPadding = 8.dp,
                roundedCornerShapeValue = 14.dp,
            )
            CurrencyDropdown(
                selectedCurrency = selectedPriceCurrency,
                isExpanded = isCurrencyDropdownExpanded,
                onDropdownClick = { onCurrencyDropdownClick() },
                onCurrencySelected = { onCurrencySelected(it) },
                onDismiss = { onCurrencyDropdownDismiss() },
                withAny = true,
                labelSize = REGULAR,
                modifier = Modifier.weight(2.5f)
            )
            PriceUnitDropdown(
                selectedUnit = selectedPriceUnit,
                isExpanded = isPriceUnitDropdownExpanded,
                onDropdownClick = { onPriceUnitDropdownClick() },
                onUnitSelected = { onPriceUnitSelected(it) },
                onDismiss = { onPriceUnitDropdownDismiss() },
                withAny = true,
                labelSize = REGULAR,
                horizontalPadding = 8.dp,
                modifier = Modifier.weight(3f)
            )
        }
    }
}

@Composable
fun CategoryFilter(
    animationDuration: Int,
    isCategoryFilterDropdownExpanded: Boolean,
    onCategoryFilterDropdownClick: () -> Unit,

    selectedSuperCategory: OfferSuperCategory?,
    isSuperCategoryFilterDropdownExpanded: Boolean,
    onSuperCategoryFilterDropdownClick: () -> Unit,
    onSuperCategoryFilterClick: (OfferSuperCategory) -> Unit,
    onSuperCategoryFilterDropdownDismiss: () -> Unit,
    onDisabledSuperCategoryClick: () -> Unit,

    selectedSubcategory: OfferCategory?,
    isSubcategoryFilterDropdownExpanded: Boolean,
    onSubcategoryFilterDropdownClick: () -> Unit,
    onSubcategoryFilterClick: (OfferCategory) -> Unit,
    onSubcategoryFilterDropdownDismiss: () -> Unit,
    onDisabledSubcategoryClick: () -> Unit,

    selectedOfferType: OfferType?,
) {
    val superCategories = remember(selectedOfferType) {
        if (selectedOfferType != null) {
            OfferSuperCategory.getParentsByType(selectedOfferType)
        } else {
            emptyList()
        }
    }

    val subCategories = remember(selectedSuperCategory) {
        if (selectedSuperCategory != null) {
            OfferCategory.getSubcategories(selectedSuperCategory)
        } else {
            emptyList()
        }
    }

    FilterDropdown(
        label = "Categories",
        isExpanded = isCategoryFilterDropdownExpanded,
        contentColor = onColorBackgroundDarker,
        onDropdownClick = { onCategoryFilterDropdownClick() }
    )
    DropdownExpandAnimation(
        animationDuration = animationDuration,
        isExpanded = isCategoryFilterDropdownExpanded
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryDropdown(
                items = superCategories,
                selectedItem = selectedSuperCategory,
                isExpanded = isSuperCategoryFilterDropdownExpanded,
                onDropdownClick = { onSuperCategoryFilterDropdownClick() },
                onItemSelected = { onSuperCategoryFilterClick(it as OfferSuperCategory) },
                onDismiss = { onSuperCategoryFilterDropdownDismiss() },
                isEnabled = selectedOfferType != null,
                onDisabledClick = { onDisabledSuperCategoryClick() },
                placeholder = "Super category",
                modifier = Modifier.weight(1f)
            )
            CategoryDropdown(
                items = subCategories,
                selectedItem = selectedSubcategory,
                isExpanded = isSubcategoryFilterDropdownExpanded,
                onDropdownClick = { onSubcategoryFilterDropdownClick() },
                onItemSelected = { onSubcategoryFilterClick(it as OfferCategory) },
                onDismiss = { onSubcategoryFilterDropdownDismiss() },
                isEnabled = selectedSuperCategory != null,
                onDisabledClick = { onDisabledSubcategoryClick() },
                placeholder = "Subcategory",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CityAndItemCondition(
    selectedOfferType: OfferType?,
    cityState: TextFieldState,
    selectedItemCondition: ItemCondition?,
    isItemConditionFilterDropdownExpanded: Boolean,
    onItemConditionFilterDropdownClick: () -> Unit,
    onItemConditionFilterClick: (ItemCondition) -> Unit,
    onItemConditionFilterDropdownDismiss: () -> Unit,
    onDisabledCityClick: () -> Unit,
    onDisabledItemConditionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleServiceTextField(
            state = cityState,
            onFocus = {},
            errorText = null,
            verticalPadding = 8.dp,
            horizontalPadding = 16.dp,
            roundedCornerShapeValue = 14.dp,
            icon = IconType.Vector(LocalisationOutlined),
            placeholder = "City",
            isEnabled = selectedOfferType == OfferType.SERVICE,
            onDisabledClick = { onDisabledCityClick() },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
        ItemConditionDropdown(
            selectedCondition = selectedItemCondition,
            isExpanded = isItemConditionFilterDropdownExpanded,
            onDropdownClick = { onItemConditionFilterDropdownClick() },
            onConditionSelected = { onItemConditionFilterClick(it) },
            onDismiss = { onItemConditionFilterDropdownDismiss() },
            isEnabled = selectedOfferType == OfferType.PRODUCT,
            onDisabledClick = { onDisabledItemConditionClick() },
            withAny = true,
            labelSize = MEDIUM,
            horizontalPadding = 8.dp,
            verticalPadding = 8.dp,
            minHeight = false,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
fun FilterButtonsSection(
    onApplyFiltersButtonClick: () -> Unit,
    onClearFiltersButtonClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        SimpleServiceButton(
            text = "Clear filters",
            isAnimated = false,
            textFontSize = MEDIUM,
            icon = IconType.Vector(CloseFilled),
            iconTint = onColorBackgroundDarker,
            backgroundColor = ColorSecondary,
            textColor = onColorBackgroundDarker,
            onClick = { onClearFiltersButtonClick() },
            buttonPadding = 8.dp,
            roundedCornerShapeValue = 14.dp,
            modifier = Modifier.weight(1f)
        )
        SimpleServiceButton(
            text = "Apply filters",
            isAnimated = false,
            textFontSize = MEDIUM,
            icon = IconType.Vector(CheckFilled),
            onClick = { onApplyFiltersButtonClick() },
            buttonPadding = 8.dp,
            roundedCornerShapeValue = 14.dp,
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
fun FilterDropdown(
    label: String,
    isExpanded: Boolean,
    contentColor: Color,
    onDropdownClick: () -> Unit,
) {
    Surface(
        color = ColorSecondary,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .clickable { onDropdownClick() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = MEDIUM,
                fontFamily = momoFont(),
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp)
            )
            Icon(
                imageVector = if (isExpanded) ArrowUp else ArrowDown,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

