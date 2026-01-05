package com.biernatmdev.simple_service.features.home.make_module.presentation.wizard

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.domain.enums.CategoryDisplayable
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferPriceUnit
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferSuperCategory
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.enums.TransactionType
import com.biernatmdev.simple_service.core.ui.components.FullScreenImageViewer
import com.biernatmdev.simple_service.core.ui.components.PhotoCarousel
import com.biernatmdev.simple_service.core.ui.components.dropdowns.CurrencyDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.ItemConditionDropdown
import com.biernatmdev.simple_service.core.ui.components.dropdowns.PriceUnitDropdown
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceButton
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceSnackbar
import com.biernatmdev.simple_service.core.ui.components.SimpleServiceTextField
import com.biernatmdev.simple_service.core.ui.components.rememberOvershootScale
import com.biernatmdev.simple_service.core.ui.components.rememberOvershootScales
import com.biernatmdev.simple_service.core.ui.models.IconType
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.ui.theme.ColorBackground
import com.biernatmdev.simple_service.core.ui.theme.ColorPrimary
import com.biernatmdev.simple_service.core.ui.theme.ColorSecondary
import com.biernatmdev.simple_service.core.ui.theme.FontSize.EXTRA_MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.MEDIUM
import com.biernatmdev.simple_service.core.ui.theme.FontSize.REGULAR
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SEMI_LARGE
import com.biernatmdev.simple_service.core.ui.theme.FontSize.SMALL
import com.biernatmdev.simple_service.core.ui.theme.LineHeight
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.AddFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowDown
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.ArrowUp
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.BackFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CategoryOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CheckFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Clean
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.CloseFilled
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.DescriptionOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LocalisationOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.LocationOutlined
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Money
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Offering
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Product
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Requesting
import com.biernatmdev.simple_service.core.ui.theme.Resources.Icon.Service
import com.biernatmdev.simple_service.core.ui.theme.momoFont
import com.biernatmdev.simple_service.core.ui.theme.onColorBackground
import com.biernatmdev.simple_service.core.ui.theme.onColorBackgroundDarker
import com.biernatmdev.simple_service.core.ui.theme.onColorPrimary
import com.biernatmdev.simple_service.features.home.make_module.domain.AddOfferWizardStep
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddOfferWizardScreen(
    viewModel: AddOfferWizardViewModel = koinViewModel(),
    navigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    BackHandler {
        viewModel.onEvent(AddOfferWizardEvent.OnPreviousStepClick)
    }

    LaunchedEffect(true) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddOfferWizardEffect.NavigateBack -> navigateBack()
                AddOfferWizardEffect.CreateOffer -> navigateBack()
                is AddOfferWizardEffect.ShowSnackbar -> {
                    val message = effect.message.asString(context)
                    snackbar.showSnackbar(message)
                }
            }
        }
    }

    AddOfferWizardScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        titleState = viewModel.titleState,
        priceState = viewModel.priceState,
        descriptionState = viewModel.descriptionState,
        cityState = viewModel.cityState,
        snackbar = snackbar,
    )
}

@Composable
fun AddOfferWizardScreenContent(
    state: AddOfferWizardState,
    onEvent: (AddOfferWizardEvent) -> Unit,
    titleState: TextFieldState,
    priceState: TextFieldState,
    descriptionState: TextFieldState,
    cityState: TextFieldState,
    snackbar: SnackbarHostState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        WizardTopNavBar(
            modifier = Modifier.align(Alignment.TopCenter),
            onBackClick = { onEvent(AddOfferWizardEvent.OnPreviousStepClick) },
            onCloseClick = { onEvent(AddOfferWizardEvent.OnCloseClick) },
        )

        WizardMainSection(
            modifier = Modifier
                .align(Alignment.Center),
            currentStep = state.wizardStep,
            titleState = titleState,
            priceState = priceState,
            titleErrorMsg = state.titleError,
            priceErrorMsg = state.priceError,
            onEvent = onEvent,
            selectedCurrency = state.selectedCurrency,
            isCurrencyDropdownExpanded = state.isCurrencyDropdownExpanded,
            selectedOfferType = state.selectedOfferType,
            selectedTransactionType = state.selectedTransactionType,
            transactionTypeErrorMsg = state.transactionTypeError,
            offerTypeErrorMsg = state.offerTypeError,
            selectedCategory = state.selectedCategory,
            selectedSuperCategory = state.selectedSuperCategory,
            isCategoryExpanded = state.isCategoryExpanded,
            isSuperCategoryExpanded = state.isSuperCategoryExpanded,
            categoryErrorMsg = state.categoryError,
            superCategoryErrorMsg = state.superCategoryError,
            selectedPriceUnit = state.selectedPriceUnit,
            isPriceUnitDropdownExpanded = state.isPriceUnitDropdownExpanded,
            descriptionState = descriptionState,
            descriptionErrorMsg = state.descriptionError,
            cityState = cityState,
            cityErrorMsg = state.cityError,
            selectedItemCondition = state.selectedItemCondition,
            isItemConditionDropdownExpanded = state.isItemConditionDropdownExpanded,
            selectedPhotos = state.selectedPhotos,
        )

        WizardBottomSection(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentStep = state.wizardStep,
            isLoading = state.isLoading,
            onEvent = onEvent,
        )

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.TopCenter),
            snackbar = { data ->
                SimpleServiceSnackbar(data)
            }
        )
    }
}

@Composable
fun WizardMainSection(
    modifier: Modifier = Modifier,
    currentStep: AddOfferWizardStep,
    titleState: TextFieldState,
    priceState: TextFieldState,
    titleErrorMsg: UiText?,
    priceErrorMsg: UiText?,
    onEvent: (AddOfferWizardEvent) -> Unit,
    selectedCurrency: String,
    isCurrencyDropdownExpanded: Boolean,
    selectedOfferType: OfferType?,
    selectedTransactionType: TransactionType?,
    transactionTypeErrorMsg: UiText?,
    offerTypeErrorMsg: UiText?,
    selectedCategory: OfferCategory?,
    selectedSuperCategory: OfferSuperCategory?,
    isCategoryExpanded: Boolean,
    isSuperCategoryExpanded: Boolean,
    categoryErrorMsg: UiText?,
    superCategoryErrorMsg: UiText?,
    selectedPriceUnit: OfferPriceUnit,
    isPriceUnitDropdownExpanded: Boolean,
    descriptionState: TextFieldState,
    descriptionErrorMsg: UiText?,
    cityState: TextFieldState,
    cityErrorMsg: UiText?,
    selectedItemCondition: ItemCondition,
    isItemConditionDropdownExpanded: Boolean,
    selectedPhotos: List<Uri>,
) {
    AnimatedContent(
        targetState = currentStep,
        label = "WizardTransition",
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 60.dp, bottom = 250.dp),
        contentAlignment = Alignment.Center,
        transitionSpec = {
            if (targetState.index > initialState.index) {
                (slideInHorizontally(tween(400)) { width -> width } + fadeIn(tween(400)))
                    .togetherWith(slideOutHorizontally(tween(400)) { width -> -width } + fadeOut(
                        tween(400)
                    ))
            } else {
                (slideInHorizontally(tween(400)) { width -> -width } + fadeIn(tween(400)))
                    .togetherWith(slideOutHorizontally(tween(400)) { width -> width } + fadeOut(
                        tween(400)
                    ))
            }
        }
    ) { targetStep ->
        when (targetStep) {
            AddOfferWizardStep.INFO_STEP -> InfoStepContent()
            AddOfferWizardStep.TRANSACTION_TYPE_PICKER_STEP -> TransactionTypeStepContent(
                onEvent = onEvent,
                selectedTransactionType = selectedTransactionType,
                transactionTypeErrorMsg = transactionTypeErrorMsg,
            )

            AddOfferWizardStep.OFFER_TYPE_PICKER_STEP -> OfferTypeStepContent(
                onEvent = onEvent,
                selectedOfferType = selectedOfferType,
                offerTypeErrorMsg = offerTypeErrorMsg,
            )

            AddOfferWizardStep.BASIC_INFO_PICKER_STEP -> BasicInfoStepContent(
                titleState = titleState,
                priceState = priceState,
                titleErrorMsg = titleErrorMsg,
                priceErrorMsg = priceErrorMsg,
                onEvent = onEvent,
                selectedCurrency = selectedCurrency,
                isCurrencyDropdownExpanded = isCurrencyDropdownExpanded,
                selectedPriceUnit = selectedPriceUnit,
                isPriceUnitDropdownExpanded = isPriceUnitDropdownExpanded,
            )

            AddOfferWizardStep.CATEGORY_PICKER_STEP -> CategoryStepContent(
                selectedSuperCategory = selectedSuperCategory,
                isCategoryExpanded = isCategoryExpanded,
                isSuperCategoryExpanded = isSuperCategoryExpanded,
                selectedOfferType = selectedOfferType,
                selectedCategory = selectedCategory,
                onEvent = onEvent,
                categoryErrorMsg = categoryErrorMsg,
                superCategoryErrorMsg = superCategoryErrorMsg,
            )

            AddOfferWizardStep.DETAILS_PICKER_STEP -> DetailsStepContent(
                onEvent = onEvent,
                descriptionState = descriptionState,
                descriptionErrorMsg = descriptionErrorMsg,
                cityState = cityState,
                cityErrorMsg = cityErrorMsg,
                selectedItemCondition = selectedItemCondition,
                isItemConditionDropdownExpanded = isItemConditionDropdownExpanded,
                selectedOfferType = selectedOfferType,
            )

            AddOfferWizardStep.PHOTO_PICKER_STEP -> PhotoStepContent(
                selectedPhotos = selectedPhotos,
                onEvent = onEvent,
            )

            AddOfferWizardStep.SUMMARY_STEP -> SummaryStepContent(
                selectedTransactionType = selectedTransactionType,
                selectedOfferType = selectedOfferType,
                titleState = titleState,
                priceState = priceState,
                selectedCurrency = selectedCurrency,
                selectedPriceUnit = selectedPriceUnit,
                selectedSuperCategory = selectedSuperCategory,
                selectedCategory = selectedCategory,
                descriptionState = descriptionState,
                cityState = cityState,
                selectedItemCondition = selectedItemCondition,
                selectedPhotos = selectedPhotos,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
fun WizardTopNavBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackClick() },
        ) {
            Icon(
                imageVector = BackFilled,
                tint = onColorBackground,
                contentDescription = stringResource(R.string.arrow_back),
                modifier = Modifier
                    .size(32.dp)
            )
        }

        IconButton(
            onClick = { onCloseClick() },
        ) {
            Icon(
                imageVector = CloseFilled,
                tint = onColorBackground,
                contentDescription = stringResource(R.string.close),
                modifier = Modifier
                    .size(48.dp)
            )
        }
    }
}

@Composable
fun WizardBottomSection(
    modifier: Modifier = Modifier,
    currentStep: AddOfferWizardStep,
    isLoading: Boolean,
    onEvent: (AddOfferWizardEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WizardStepIndicator(currentStep = currentStep)

            Spacer(Modifier.height(48.dp))

            if (currentStep.isLastStep) {
                SimpleServiceButton(
                    text = UiText.StringResource(R.string.wizard_screen_btn_label_publish).asString(),
                    isAnimated = true,
                    isLoading = isLoading,
                    isIconLeading = false,
                    isIconAtEdge = true,
                    icon = IconType.Vector(CheckFilled),
                    onClick = { onEvent(AddOfferWizardEvent.OnCreateOfferClick) },
                    modifier = Modifier.fillMaxWidth(),
                )
            } else if (currentStep.isFirstStep) {
                SimpleServiceButton(
                    text = UiText.StringResource(R.string.wizard_screen_btn_label_start).asString(),
                    isAnimated = false,
                    isIconLeading = false,
                    isIconAtEdge = true,
                    icon = IconType.Vector(ArrowFilled),
                    onClick = { onEvent(AddOfferWizardEvent.OnNextStepClick) },
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                SimpleServiceButton(
                    text = UiText.StringResource(R.string.wizard_screen_btn_label_next).asString(),
                    isAnimated = false,
                    isIconLeading = false,
                    isIconAtEdge = true,
                    icon = IconType.Vector(ArrowFilled),
                    onClick = { onEvent(AddOfferWizardEvent.OnNextStepClick) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun WizardStepIndicator(
    currentStep: AddOfferWizardStep,
    modifier: Modifier = Modifier,
) {
    val totalSteps = AddOfferWizardStep.totalSteps

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val isSelected = index <= currentStep.index

            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) ColorPrimary else onColorBackground,
                label = "color"
            )

            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(animatedColor)
            )
        }
    }
}

@Composable
fun InfoStepContent() {
    val scale = rememberOvershootScale()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = AddFilled,
            contentDescription = null,
            modifier = Modifier
                .scale(scale.value)
                .size(220.dp),
            tint = ColorPrimary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_0_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_0_subtitle).asString(),
            color = onColorBackgroundDarker,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
    }

}

@Composable
fun TransactionTypeStepContent(
    onEvent: (AddOfferWizardEvent) -> Unit,
    selectedTransactionType: TransactionType?,
    transactionTypeErrorMsg: UiText?,
) {
    val scales = rememberOvershootScales(count = 2)

    val requestAlpha by animateFloatAsState(
        targetValue = if (selectedTransactionType == null || selectedTransactionType == TransactionType.REQUEST) 1f else 0.7f,
        label = "alpha_request"
    )
    val offerAlpha by animateFloatAsState(
        targetValue = if (selectedTransactionType == null || selectedTransactionType == TransactionType.OFFER) 1f else 0.7f,
        label = "alpha_offer"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_1_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onEvent(AddOfferWizardEvent.OnTransactionTypeClick(TransactionType.REQUEST)) }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Requesting),
                contentDescription = null,
                modifier = Modifier
                    .scale(scales[0].value)
                    .size(160.dp)
                    .graphicsLayer(alpha = requestAlpha),
                tint = Color.Unspecified,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = UiText.StringResource(R.string.wizard_screen_text_step_1_subtitle_offer).asString(),
                color = when {
                    selectedTransactionType == TransactionType.REQUEST -> ColorPrimary
                    transactionTypeErrorMsg != null -> Color.Red
                    else -> onColorBackgroundDarker
                },
                fontFamily = momoFont(),
                fontSize = MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.MEDIUM,
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onEvent(AddOfferWizardEvent.OnTransactionTypeClick(TransactionType.OFFER)) }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Offering),
                contentDescription = null,
                modifier = Modifier
                    .scale(scales[1].value)
                    .size(160.dp)
                    .graphicsLayer(alpha = offerAlpha),
                tint = Color.Unspecified,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = UiText.StringResource(R.string.wizard_screen_text_step_1_subtitle_request).asString(),
                color = when {
                    selectedTransactionType == TransactionType.OFFER -> ColorPrimary
                    transactionTypeErrorMsg != null -> Color.Red
                    else -> onColorBackgroundDarker
                },
                fontFamily = momoFont(),
                fontSize = MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.MEDIUM,
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = transactionTypeErrorMsg?.asString() ?: "",
            fontSize = SMALL,
            fontFamily = momoFont(),
            color = Color.Red,
        )
    }
}

@Composable
fun OfferTypeStepContent(
    onEvent: (AddOfferWizardEvent) -> Unit,
    selectedOfferType: OfferType?,
    offerTypeErrorMsg: UiText?,
) {
    val scales = rememberOvershootScales(count = 2)

    val productAlpha by animateFloatAsState(
        targetValue = if (selectedOfferType == null || selectedOfferType == OfferType.PRODUCT) 1f else 0.7f,
        label = "alpha_product"
    )
    val serviceAlpha by animateFloatAsState(
        targetValue = if (selectedOfferType == null || selectedOfferType == OfferType.SERVICE) 1f else 0.7f,
        label = "alpha_service"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_2_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onEvent(AddOfferWizardEvent.OnOfferTypeClick(OfferType.PRODUCT)) }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Product),
                contentDescription = null,
                modifier = Modifier
                    .scale(scales[0].value)
                    .size(160.dp)
                    .graphicsLayer(alpha = productAlpha),
                tint = Color.Unspecified,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = UiText.StringResource(R.string.wizard_screen_text_step_2_subtitle_product).asString(),
                color = when {
                    selectedOfferType == OfferType.PRODUCT -> ColorPrimary
                    offerTypeErrorMsg != null -> Color.Red
                    else -> onColorBackgroundDarker
                },
                fontFamily = momoFont(),
                fontSize = MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.MEDIUM,
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { onEvent(AddOfferWizardEvent.OnOfferTypeClick(OfferType.SERVICE)) }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Service),
                contentDescription = null,
                modifier = Modifier
                    .scale(scales[1].value)
                    .size(160.dp)
                    .graphicsLayer(alpha = serviceAlpha),
                tint = Color.Unspecified,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = UiText.StringResource(R.string.wizard_screen_text_step_2_subtitle_service).asString(),
                color = when {
                    selectedOfferType == OfferType.SERVICE -> ColorPrimary
                    offerTypeErrorMsg != null -> Color.Red
                    else -> onColorBackgroundDarker
                },
                fontFamily = momoFont(),
                fontSize = MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.MEDIUM,
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = offerTypeErrorMsg?.asString() ?: "",
            fontSize = SMALL,
            fontFamily = momoFont(),
            color = Color.Red,
        )
    }
}

@Composable
fun BasicInfoStepContent(
    titleState: TextFieldState,
    priceState: TextFieldState,
    titleErrorMsg: UiText?,
    priceErrorMsg: UiText?,
    onEvent: (AddOfferWizardEvent) -> Unit,
    selectedCurrency: String,
    isCurrencyDropdownExpanded: Boolean,
    selectedPriceUnit: OfferPriceUnit,
    isPriceUnitDropdownExpanded: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_3_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(60.dp))

        SimpleServiceTextField(
            state = titleState,
            onFocus = { onEvent(AddOfferWizardEvent.OnTitleFocused) },
            errorText = titleErrorMsg,
            placeholder = "Title",
        )

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SimpleServiceTextField(
                    isPhoneNumber = true,
                    state = priceState,
                    onFocus = { onEvent(AddOfferWizardEvent.OnPriceFocused) },
                    errorText = priceErrorMsg,
                    placeholder = "Price",
                )
            }

            Spacer(Modifier.width(12.dp))

            CurrencyDropdown(
                selectedCurrency = selectedCurrency,
                isExpanded = isCurrencyDropdownExpanded,
                onDropdownClick = { onEvent(AddOfferWizardEvent.OnCurrencyDropdownClick) },
                onCurrencySelected = { onEvent(AddOfferWizardEvent.OnCurrencySelected(it)) },
                onDismiss = { onEvent(AddOfferWizardEvent.OnCurrencyDropdownDismiss) }
            )

            Spacer(Modifier.width(12.dp))

            PriceUnitDropdown(
                selectedUnit = selectedPriceUnit,
                isExpanded = isPriceUnitDropdownExpanded,
                onDropdownClick = { onEvent(AddOfferWizardEvent.OnPriceUnitDropdownClick) },
                onUnitSelected = { onEvent(AddOfferWizardEvent.OnPriceUnitSelected(it)) },
                onDismiss = { onEvent(AddOfferWizardEvent.OnPriceUnitDropdownDismiss) },
            )
        }
    }
}

@Composable
fun CategoryStepContent(
    selectedCategory: OfferCategory?,
    selectedSuperCategory: OfferSuperCategory?,
    isCategoryExpanded: Boolean,
    isSuperCategoryExpanded: Boolean,
    selectedOfferType: OfferType?,
    onEvent: (AddOfferWizardEvent) -> Unit,
    superCategoryErrorMsg: UiText?,
    categoryErrorMsg: UiText?,
) {
    val superCategories = remember(selectedOfferType) {
        OfferSuperCategory.getParentsByType(selectedOfferType ?: OfferType.PRODUCT)
    }
    val subCategories = remember(selectedSuperCategory) {
        selectedSuperCategory?.let { OfferCategory.getSubcategories(it) } ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_4_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(48.dp))
        ExpandableCategoryPicker(
            label = selectedSuperCategory?.displayName?.asString() ?: "Super category",
            isExpanded = isSuperCategoryExpanded,
            entries = superCategories,
            onDropdownClick = { onEvent(AddOfferWizardEvent.ToggleSuperCategorySection) },
            onChipClick = { onEvent(AddOfferWizardEvent.OnSuperCategorySelected(it as OfferSuperCategory)) },
            isEnabled = true,
            selectedChip = selectedSuperCategory,
            isError = superCategoryErrorMsg != null,
        )
        Spacer(Modifier.height(24.dp))
        ExpandableCategoryPicker(
            label = selectedCategory?.displayName?.asString() ?: "Category",
            isExpanded = isCategoryExpanded,
            entries = subCategories,
            onDropdownClick = { onEvent(AddOfferWizardEvent.ToggleCategorySection) },
            onChipClick = { onEvent(AddOfferWizardEvent.OnCategorySelected(it as OfferCategory)) },
            isEnabled = selectedSuperCategory != null,
            selectedChip = selectedCategory,
            isError = categoryErrorMsg != null,
        )
        Spacer(Modifier.height(100.dp))
        if (categoryErrorMsg != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = categoryErrorMsg.asString(),
                fontSize = SMALL,
                fontFamily = momoFont(),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DetailsStepContent(
    onEvent: (AddOfferWizardEvent) -> Unit,
    descriptionState: TextFieldState,
    descriptionErrorMsg: UiText?,
    cityState: TextFieldState,
    cityErrorMsg: UiText?,
    selectedItemCondition: ItemCondition,
    isItemConditionDropdownExpanded: Boolean,
    selectedOfferType: OfferType?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_5_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(58.dp))
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_5_subtitle_description).asString(),
            color = onColorBackgroundDarker,
            fontFamily = momoFont(),
            fontSize = EXTRA_MEDIUM,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.EXTRA_MEDIUM,
        )
        Spacer(Modifier.height(24.dp))
        SimpleServiceTextField(
            state = descriptionState,
            onFocus = { onEvent(AddOfferWizardEvent.OnDescriptionFocused) },
            placeholder = "description",
            icon = IconType.Vector(DescriptionOutlined),
            errorText = descriptionErrorMsg,
            isMultiline = true,
            minLines = 5,
        )
        Spacer(Modifier.height(36.dp))
        if (selectedOfferType == OfferType.PRODUCT) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = UiText.StringResource(R.string.wizard_screen_text_step_5_subtitle_condition).asString(),
                    color = onColorBackgroundDarker,
                    fontFamily = momoFont(),
                    fontSize = EXTRA_MEDIUM,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LineHeight.EXTRA_MEDIUM,
                )
                ItemConditionDropdown(
                    selectedCondition = selectedItemCondition,
                    isExpanded = isItemConditionDropdownExpanded,
                    onDropdownClick = { onEvent(AddOfferWizardEvent.OnItemConditionDropdownClick) },
                    onConditionSelected = { onEvent(AddOfferWizardEvent.OnItemConditionSelected(it)) },
                    onDismiss = { onEvent(AddOfferWizardEvent.OnItemConditionDropdownDismiss) },
                )
            }
            Spacer(Modifier.height(16.dp))
        } else {
            Text(
                text = UiText.StringResource(R.string.wizard_screen_text_step_5_subtitle_city).asString(),
                color = onColorBackgroundDarker,
                fontFamily = momoFont(),
                fontSize = EXTRA_MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.EXTRA_MEDIUM,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(24.dp))
            SimpleServiceTextField(
                state = cityState,
                onFocus = { onEvent(AddOfferWizardEvent.OnCityFocused) },
                icon = IconType.Vector(LocalisationOutlined),
                placeholder = "city",
                errorText = cityErrorMsg,
            )
        }
    }

}

@Composable
fun PhotoStepContent(
    selectedPhotos: List<Uri>,
    onEvent: (AddOfferWizardEvent) -> Unit,
) {
    var fullscreenImage by rememberSaveable { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5),
        onResult = { uris ->
            onEvent(AddOfferWizardEvent.OnPhotosSelected(uris))
        }
    )

    val launchGallery = {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_6_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_6_subtitle).asString(),
            color = onColorBackgroundDarker,
            fontFamily = momoFont(),
            fontSize = EXTRA_MEDIUM,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.EXTRA_MEDIUM,
        )
        Spacer(Modifier.height(60.dp))
        PhotoCarousel(
            photos = selectedPhotos,
            onRemoveClick = { photo -> onEvent(AddOfferWizardEvent.OnPhotoRemoved(photo as Uri)) },
            onAddPhotoClick = { launchGallery() },
            onImageClick = { photo -> fullscreenImage = photo as Uri },
        )
    }

    FullScreenImageViewer(
        imageModel = fullscreenImage,
        onDismiss = { fullscreenImage = null }
    )
}

@Composable
fun SummaryStepContent(
    onEvent: (AddOfferWizardEvent) -> Unit,
    selectedTransactionType: TransactionType?,
    selectedOfferType: OfferType?,
    titleState: TextFieldState,
    priceState: TextFieldState,
    selectedCurrency: String,
    selectedPriceUnit: OfferPriceUnit,
    selectedSuperCategory: OfferSuperCategory?,
    selectedCategory: OfferCategory?,
    descriptionState: TextFieldState,
    cityState: TextFieldState,
    selectedItemCondition: ItemCondition,
    selectedPhotos: List<Uri>,
) {
    var fullscreenImage by rememberSaveable { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5),
        onResult = { uris ->
            onEvent(AddOfferWizardEvent.OnPhotosSelected(uris))
        }
    )

    val launchGallery = {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = UiText.StringResource(R.string.wizard_screen_text_step_7_title).asString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
        )
        Spacer(Modifier.height(36.dp))
        PhotoCarousel(
            photos = selectedPhotos,
            onRemoveClick = { photo -> onEvent(AddOfferWizardEvent.OnPhotoRemoved(photo as Uri)) },
            onAddPhotoClick = { launchGallery() },
            onImageClick = { photo -> fullscreenImage = photo as Uri },
            modifier = Modifier.height(300.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = titleState.text.toString(),
            color = onColorBackground,
            fontFamily = momoFont(),
            fontSize = LARGE,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.LARGE,
            modifier = Modifier.clickable{
                onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.BASIC_INFO_PICKER_STEP))
            }
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummarySquareCard(
                painter = if (selectedOfferType == OfferType.PRODUCT) painterResource(Product) else painterResource(
                    Service
                ),
                title = "${selectedOfferType?.displayName?.asString()}",
                onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.OFFER_TYPE_PICKER_STEP)) },
                modifier = Modifier.weight(1f),
            )
            SummarySquareCard(
                painter = if (selectedTransactionType == TransactionType.OFFER) painterResource(
                    Offering
                ) else painterResource(Requesting),
                title = "${selectedTransactionType?.displayName?.asString()}",
                onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.TRANSACTION_TYPE_PICKER_STEP)) },
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(16.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = Int.MAX_VALUE,
        ) {
            SummaryFlatCard(
                label = selectedSuperCategory?.displayName?.asString() ?: "",
                icon = IconType.Vector(CategoryOutlined),
                onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.CATEGORY_PICKER_STEP)) }
            )
            SummaryFlatCard(
                label = selectedCategory?.displayName?.asString() ?: "",
                icon = IconType.Vector(CategoryOutlined),
                onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.CATEGORY_PICKER_STEP)) }
            )
            SummaryFlatCard(
                label = "${priceState.text}  $selectedCurrency  /  ${selectedPriceUnit.displayName.asString()}",
                icon = IconType.Drawable(Money),
                onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.BASIC_INFO_PICKER_STEP)) }
            )
            if (selectedOfferType == OfferType.SERVICE) {
                SummaryFlatCard(
                    label = cityState.text.toString(),
                    icon = IconType.Vector(LocationOutlined),
                    onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.DETAILS_PICKER_STEP)) }
                )
            } else {
                SummaryFlatCard(
                    label = selectedItemCondition.displayName.asString(),
                    icon = IconType.Drawable(Clean),
                    onClick = { onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.DETAILS_PICKER_STEP)) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        onEvent(AddOfferWizardEvent.OnGoToStep(AddOfferWizardStep.DETAILS_PICKER_STEP))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
        ){
            Text(
                text = UiText.StringResource(R.string.wizard_screen_text_step_7_subtitle_description).asString(),
                color = onColorBackground,
                fontFamily = momoFont(),
                fontSize = SEMI_LARGE,
                fontWeight = FontWeight.Bold,
                lineHeight = LineHeight.SEMI_LARGE,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = ColorSecondary, shape = RoundedCornerShape(22.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = descriptionState.text.toString(),
                    color = onColorBackgroundDarker,
                    fontFamily = momoFont(),
                    fontSize = REGULAR,
                    fontWeight = FontWeight.Bold,
                    lineHeight = LineHeight.REGULAR,
                )
            }
        }
        Spacer(Modifier.height(36.dp))
    }

    FullScreenImageViewer(
        imageModel = fullscreenImage,
        onDismiss = { fullscreenImage = null }
    )
}

@Composable
fun SummarySquareCard(
    painter: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .background(color = ColorSecondary, shape = RoundedCornerShape(22.dp))
            .padding(16.dp)
            .clickable{ onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painter,
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            color = onColorBackgroundDarker,
            fontFamily = momoFont(),
            fontSize = MEDIUM,
            fontWeight = FontWeight.Bold,
            lineHeight = LineHeight.MEDIUM,
        )
    }
}

@Composable
fun SummaryFlatCard(
    label: String,
    icon: IconType,
    onClick: () -> Unit,
) {
    Surface(
        color = ColorSecondary,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clickable{ onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (icon) {
                is IconType.Vector -> {
                    Icon(
                        imageVector = icon.imageVector,
                        tint = onColorBackgroundDarker,
                        contentDescription = null,
                    )
                }
                is IconType.Drawable -> {
                    Icon(
                        painter = painterResource(id = icon.id),
                        tint = onColorBackgroundDarker,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                color = onColorBackgroundDarker,
                fontFamily = momoFont(),
                fontSize = MEDIUM,
                fontWeight = FontWeight.Bold,
                lineHeight = MEDIUM,
            )
        }
    }
}

@Composable
fun ExpandableCategoryPicker(
    label: String,
    isExpanded: Boolean,
    entries: List<CategoryDisplayable>,
    onDropdownClick: () -> Unit,
    onChipClick: (CategoryDisplayable) -> Unit,
    isEnabled: Boolean = true,
    selectedChip: CategoryDisplayable?,
    isError: Boolean = false,
) {
    val contentColor = when {
        isError -> Color.Red
        else -> onColorBackgroundDarker
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            color = if (!isEnabled) ColorBackground else ColorSecondary,
            border = if (!isEnabled) {
                BorderStroke(
                    width = 1.dp,
                    color = onColorBackgroundDarker,
                )
            } else {
                null
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .clickable(
                    enabled = isEnabled,
                    onClick = { onDropdownClick() }
                )
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = SEMI_LARGE,
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

        val animationDuration = 600
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
                Spacer(Modifier.height(24.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    entries.forEach { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category == selectedChip,
                            onChipClick = { onChipClick(category) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: CategoryDisplayable,
    isSelected: Boolean,
    onChipClick: (CategoryDisplayable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (isSelected) ColorPrimary else ColorSecondary
    val textColor = if (isSelected) onColorPrimary else onColorBackgroundDarker

    Surface(
        color = background,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .clickable { onChipClick(category) }
    ) {
        Text(
            text = category.displayName.asString(),
            fontSize = MEDIUM,
            fontFamily = momoFont(),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}