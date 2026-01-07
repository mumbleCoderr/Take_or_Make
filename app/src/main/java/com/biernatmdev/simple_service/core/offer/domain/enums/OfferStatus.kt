package com.biernatmdev.simple_service.core.offer.domain.enums

import android.os.Parcelable
import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.ui.models.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OfferStatus(val displayName: UiText) : Parcelable {
    ACTIVE(displayName = UiText.StringResource(R.string.offer_status_name_active)),
    INACTIVE(displayName = UiText.StringResource(R.string.offer_status_name_inactive)),
}