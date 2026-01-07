package com.biernatmdev.simple_service.features.home.make_module.presentation.wizard

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.TextField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.offer.data.validation.OfferValidator
import com.biernatmdev.simple_service.core.offer.domain.enums.ItemCondition
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferStatus
import com.biernatmdev.simple_service.core.offer.domain.enums.OfferType
import com.biernatmdev.simple_service.core.offer.domain.model.Offer
import com.biernatmdev.simple_service.core.offer.domain.model.OfferException
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.ui.models.UiText
import com.biernatmdev.simple_service.core.user.domain.model.UserException
import com.biernatmdev.simple_service.core.utils.ImageUtils
import com.biernatmdev.simple_service.features.home.make_module.domain.AddOfferWizardStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddOfferWizardViewModel(
    private val repository: OfferRepository,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(AddOfferWizardState())
    val state = _state.asStateFlow()

    private val _effect = Channel<AddOfferWizardEffect>()
    val effect = _effect.receiveAsFlow()

    val titleState = TextFieldState()
    val priceState = TextFieldState()
    val descriptionState = TextFieldState()
    val cityState = TextFieldState()

    fun onEvent(event: AddOfferWizardEvent) {
        val currentStep = _state.value.wizardStep
        val currentStepIndex = currentStep.index

        when (event) {
            AddOfferWizardEvent.OnNextStepClick -> {
                val canGoNext = when (currentStep) {
                    AddOfferWizardStep.BASIC_INFO_PICKER_STEP -> validateBasicInfoForm()
                    AddOfferWizardStep.TRANSACTION_TYPE_PICKER_STEP -> validateTransactionPick()
                    AddOfferWizardStep.OFFER_TYPE_PICKER_STEP -> validateOfferPick()
                    AddOfferWizardStep.CATEGORY_PICKER_STEP -> validateCategory()
                    AddOfferWizardStep.DETAILS_PICKER_STEP -> validateDetails()
                    else -> true
                }

                if (canGoNext) {
                    val nextIndex = currentStepIndex + 1
                    if (nextIndex < AddOfferWizardStep.totalSteps) {
                        _state.value = _state.value.copy(
                            wizardStep = AddOfferWizardStep.getByIndex(nextIndex)
                        )
                    }
                }
            }
            AddOfferWizardEvent.OnCreateOfferClick -> {
                if (_state.value.isEditMode) {
                    updateOffer()
                } else {
                    createOffer()
                }
            }
            AddOfferWizardEvent.OnPreviousStepClick -> {
                val prevIndex = currentStepIndex - 1
                if (prevIndex >= 0) {
                    _state.value = _state.value.copy(
                        wizardStep = AddOfferWizardStep.getByIndex(prevIndex)
                    )
                } else {
                    _state.update { it.copy(isExitDialogVisible = true) }
                }
            }
            AddOfferWizardEvent.OnCloseClick -> {
                _state.update { it.copy(isExitDialogVisible = true) }
            }
            AddOfferWizardEvent.OnPriceFocused -> {
                _state.update { it.copy(priceError = null) }
            }
            AddOfferWizardEvent.OnTitleFocused -> {
                _state.update { it.copy(titleError = null) }
            }
            AddOfferWizardEvent.OnCurrencyDropdownClick -> {
                _state.update { it.copy(isCurrencyDropdownExpanded = !it.isCurrencyDropdownExpanded) }
            }
            is AddOfferWizardEvent.OnCurrencySelected -> {
                _state.update {
                    it.copy(
                        selectedCurrency = event.currencyCode,
                        isCurrencyDropdownExpanded = false,
                    )
                }
            }

            AddOfferWizardEvent.OnCurrencyDropdownDismiss -> {
                _state.update { it.copy(isCurrencyDropdownExpanded = false) }
            }

            AddOfferWizardEvent.OnPriceUnitDropdownClick -> {
                _state.update { it.copy(isPriceUnitDropdownExpanded = !it.isPriceUnitDropdownExpanded) }
            }
            AddOfferWizardEvent.OnPriceUnitDropdownDismiss -> {
                _state.update { it.copy(isPriceUnitDropdownExpanded = false) }
            }
            is AddOfferWizardEvent.OnPriceUnitSelected -> {
                _state.update {
                    it.copy(
                        selectedPriceUnit = event.priceUnit,
                        isPriceUnitDropdownExpanded = false,
                    )
                }
            }
            is AddOfferWizardEvent.OnOfferTypeClick -> {
                val hasTypeChanged = _state.value.selectedOfferType != event.offerType
                val nextCondition = if (event.offerType == OfferType.SERVICE) {
                    ItemCondition.NOT_APPLICABLE
                } else {
                    if (hasTypeChanged) ItemCondition.NEW else _state.value.selectedItemCondition
                }

                _state.update {
                    it.copy(
                        selectedOfferType = event.offerType,
                        offerTypeError = null,

                        selectedSuperCategory = if (hasTypeChanged) null else it.selectedSuperCategory,
                        selectedCategory = if (hasTypeChanged) null else it.selectedCategory,

                        superCategoryError = if (hasTypeChanged) null else it.superCategoryError,
                        categoryError = if (hasTypeChanged) null else it.categoryError,

                        isSuperCategoryExpanded = if (hasTypeChanged) false else it.isSuperCategoryExpanded,
                        isCategoryExpanded = if (hasTypeChanged) false else it.isCategoryExpanded,

                        selectedItemCondition = nextCondition
                    )
                }
            }
            is AddOfferWizardEvent.OnTransactionTypeClick -> {
                _state.update {
                    it.copy(
                        selectedTransactionType = event.transactionType,
                        transactionTypeError = null,
                    )
                }
            }

            is AddOfferWizardEvent.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategory = event.category,
                        isCategoryExpanded = false,
                    )
                }
            }
            is AddOfferWizardEvent.OnSuperCategorySelected -> {
                _state.update {
                    it.copy(
                        selectedSuperCategory = event.superCategory,
                        isSuperCategoryExpanded = false,
                    )
                }
            }
            AddOfferWizardEvent.ToggleCategorySection -> {
                _state.update {
                    it.copy(
                        isCategoryExpanded = !it.isCategoryExpanded,
                        categoryError = null
                    )
                }
            }
            AddOfferWizardEvent.ToggleSuperCategorySection -> {
                _state.update {
                    it.copy(
                        isSuperCategoryExpanded = !it.isSuperCategoryExpanded,
                        superCategoryError = null
                    )
                }
            }

            AddOfferWizardEvent.OnItemConditionDropdownClick -> {
                _state.update { it.copy(isItemConditionDropdownExpanded = !it.isItemConditionDropdownExpanded) }
            }
            AddOfferWizardEvent.OnItemConditionDropdownDismiss -> {
                _state.update { it.copy(isItemConditionDropdownExpanded = false) }
            }
            is AddOfferWizardEvent.OnItemConditionSelected -> {
                _state.update {
                    it.copy(
                        selectedItemCondition = event.itemCondition,
                        isItemConditionDropdownExpanded = !it.isItemConditionDropdownExpanded,
                    )
                }
            }
            AddOfferWizardEvent.OnDescriptionFocused -> {
                _state.update { it.copy(descriptionError = null) }
            }
            AddOfferWizardEvent.OnCityFocused -> {
                _state.update { it.copy(cityError = null) }
            }
            is AddOfferWizardEvent.OnPhotoRemoved -> {
                _state.update {
                    it.copy(selectedPhotos = it.selectedPhotos - event.photo)
                }
            }
            is AddOfferWizardEvent.OnPhotosSelected -> {
                _state.update {
                    it.copy(selectedPhotos = it.selectedPhotos + event.photos)
                }
            }
            is AddOfferWizardEvent.OnGoToStep -> {
                _state.update {
                    it.copy(wizardStep = event.step)
                }
            }

            AddOfferWizardEvent.OnExitDialogConfirm -> {
                _state.update { it.copy(isExitDialogVisible = false) }
                sendEffect(AddOfferWizardEffect.NavigateBack)
            }
            AddOfferWizardEvent.OnExitDialogDismiss -> {
                _state.update { it.copy(isExitDialogVisible = false) }
            }

            is AddOfferWizardEvent.InitWithOffer -> {
                val offer = event.offer

                titleState.edit { replace(0, length, offer.title) }
                priceState.edit { replace(0, length, offer.price.toString()) }
                descriptionState.edit { replace(0, length, offer.description) }
                cityState.edit { replace(0, length, offer.city) }


                _state.update {
                    it.copy(
                        isEditMode = true,
                        editingOfferId = offer.id,
                        editingOfferAuthorId = offer.authorId,

                        wizardStep = AddOfferWizardStep.SUMMARY_STEP,

                        selectedTransactionType = offer.transactionType,
                        selectedOfferType = offer.offerType,
                        selectedCategory = offer.subcategory,
                        selectedSuperCategory = offer.superCategory,
                        selectedCurrency = offer.currency,
                        selectedPriceUnit = offer.priceUnit,
                        selectedItemCondition = offer.itemCondition,

                        selectedPhotos = offer.images,
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: AddOfferWizardEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private suspend fun prepareImagesForUpload(): List<String> {
        return withContext(Dispatchers.IO) {
            state.value.selectedPhotos.mapNotNull { photoItem ->
                when (photoItem) {
                    is String -> photoItem
                    is Uri -> ImageUtils.uriToBase64(application, photoItem)
                    else -> null
                }
            }
        }
    }

    private fun createOffer() {
        _state.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            val finalPhotosBase64 = try {
                prepareImagesForUpload()
            } catch (e: Exception) {
                handleException(RuntimeException("ImageProcessingError", e))
                return@launch
            }

            val newOffer = Offer(
                id = "",
                authorId = "",
                createdAt = 0L,
                status = OfferStatus.ACTIVE,

                title = titleState.text.toString().trim(),
                price = priceState.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0,
                description = descriptionState.text.toString().trim(),

                transactionType = state.value.selectedTransactionType!!,
                offerType = state.value.selectedOfferType!!,
                subcategory = state.value.selectedCategory!!,
                superCategory = state.value.selectedSuperCategory!!,

                currency = state.value.selectedCurrency,
                priceUnit = state.value.selectedPriceUnit,

                city = cityState.text.toString().trim(),
                itemCondition = state.value.selectedItemCondition,

                images = finalPhotosBase64
            )

            repository.createOffer(newOffer)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(AddOfferWizardEffect.CreateOffer)
                }
                .onFailure { exception ->
                    handleException(exception)
                }
        }
    }


    private fun updateOffer() {
        _state.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            val finalPhotosBase64 = try {
                prepareImagesForUpload()
            } catch (e: Exception) {
                handleException(RuntimeException("ImageProcessingError", e))
                return@launch
            }

            val updatedOffer = Offer(
                id = _state.value.editingOfferId ?: "",
                authorId = _state.value.editingOfferAuthorId,
                createdAt = 0L,
                status = OfferStatus.ACTIVE,

                title = titleState.text.toString().trim(),
                price = priceState.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0,
                description = descriptionState.text.toString().trim(),

                transactionType = state.value.selectedTransactionType!!,
                offerType = state.value.selectedOfferType!!,
                subcategory = state.value.selectedCategory!!,
                superCategory = state.value.selectedSuperCategory!!,

                currency = state.value.selectedCurrency,
                priceUnit = state.value.selectedPriceUnit,

                city = cityState.text.toString().trim(),
                itemCondition = state.value.selectedItemCondition,

                images = finalPhotosBase64
            )

            repository.updateOffer(updatedOffer)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(AddOfferWizardEffect.CreateOffer)
                }
                .onFailure { exception ->
                    handleException(exception)
                }
        }
    }

    private fun validateBasicInfoForm(): Boolean {
        val title = titleState.text.toString().trim()
        val price = priceState.text.toString().trim()

        val titleErrorMsg: UiText? = OfferValidator.validateTitle(title)
        val priceErrorMsg: UiText? = OfferValidator.validatePrice(price)

        val errorMsgList = listOfNotNull(
            titleErrorMsg,
            priceErrorMsg,
        )

        _state.update {
            it.copy(
                titleError = titleErrorMsg,
                priceError = priceErrorMsg,
            )
        }

        return errorMsgList.isEmpty()
    }

    private fun validateTransactionPick(): Boolean {
        val selectedTransactionType = state.value.selectedTransactionType

        val transactionTypeErrorMsg: UiText? = OfferValidator.validateTransactionTypePick(selectedTransactionType)

        _state.update {
            it.copy(
                transactionTypeError = transactionTypeErrorMsg
            )
        }

        return transactionTypeErrorMsg == null
    }

    private fun validateOfferPick(): Boolean {
        val selectedOfferType = state.value.selectedOfferType

        val offerTypeErrorMsg: UiText? = OfferValidator.validateOfferTypePick(selectedOfferType)

        _state.update {
            it.copy(
                offerTypeError = offerTypeErrorMsg
            )
        }

        return offerTypeErrorMsg == null
    }

    private fun validateCategory(): Boolean {
        val selectedSuperCategory = state.value.selectedSuperCategory
        val selectedCategory = state.value.selectedCategory

        val categoryErrorMsg: UiText? = OfferValidator.validateCategoryPick(selectedCategory)
        val superCategoryErrorMsg: UiText? = OfferValidator.validateSuperCategoryPick(selectedSuperCategory)

        val errorMsgList = listOfNotNull(
            categoryErrorMsg,
            superCategoryErrorMsg
        )

        _state.update {
            it.copy(
                categoryError = categoryErrorMsg,
                superCategoryError = superCategoryErrorMsg,
            )
        }

        return errorMsgList.isEmpty()
    }

    private fun validateDetails(): Boolean {
        val description = descriptionState.text.toString().trim()
        val city = cityState.text.toString().trim()

        val descriptionErrorMsg: UiText? = OfferValidator.validateDescription(description)
        var cityErrorMsg: UiText? = null

        if(state.value.selectedOfferType == OfferType.SERVICE) {
            cityErrorMsg = OfferValidator.validateCity(city)
        }

        val errorMsgList = listOfNotNull(
            descriptionErrorMsg,
            cityErrorMsg
        )

        _state.update {
            it.copy(
                descriptionError = descriptionErrorMsg,
                cityError = cityErrorMsg,
            )
        }

        return errorMsgList.isEmpty()
    }

    private fun handleException(exception: Throwable) {
        _state.update { it.copy(isLoading = false) }

        val errorMessage: UiText = when (exception) {
            is OfferException.NetworkError -> UiText.StringResource(R.string.offer_exception_network)
            is OfferException.AccessDenied -> UiText.StringResource(R.string.offer_exception_access_denied)
            is OfferException.NotFound -> UiText.StringResource(R.string.offer_exception_not_found)
            is UserException.NotSignedIn -> UiText.StringResource(R.string.offer_exception_user_not_signed_in)

            is RuntimeException -> {
                if(exception.message == "ImageProcessingError") {
                    UiText.StringResource(R.string.offer_exception_image_processing)
                } else {
                    UiText.DynamicString(exception.message ?: "Unknown error")
                }
            }

            else -> UiText.DynamicString(exception.message ?: "Unknown error occurred")
        }

        _state.update {
            it.copy(generalError = errorMessage)
        }

        sendEffect(AddOfferWizardEffect.ShowSnackbar(errorMessage))
    }
}