package com.biernatmdev.simple_service.core.offer.domain.enums

import android.os.Parcelable
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TransactionType(val displayName: UiText) : Parcelable {
    OFFER(displayName = UiText.StringResource(R.string.transaction_type_name_offer)),
    REQUEST(displayName = UiText.StringResource(R.string.transaction_type_name_request)),
}