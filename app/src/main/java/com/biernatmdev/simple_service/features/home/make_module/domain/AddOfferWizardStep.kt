package com.biernatmdev.simple_service.features.home.make_module.domain

enum class AddOfferWizardStep(val index: Int) {
    INFO_STEP(0),
    TRANSACTION_TYPE_PICKER_STEP(1),
    OFFER_TYPE_PICKER_STEP(2),
    BASIC_INFO_PICKER_STEP(3),
    CATEGORY_PICKER_STEP(4),
    DETAILS_PICKER_STEP(5),
    PHOTO_PICKER_STEP(6),
    SUMMARY_STEP(7);

    companion object {
        fun getByIndex(index: Int) = entries.find { it.index == index } ?: INFO_STEP
        val totalSteps = entries.size
    }

    val isLastStep: Boolean
        get() = index == totalSteps - 1

    val isFirstStep: Boolean
        get() = index == 0
}