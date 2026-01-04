package com.biernatmdev.simple_service.core.offer.domain.model

sealed class OfferException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    object AccessDenied : OfferException()

    object NotFound : OfferException()

    object NetworkError : OfferException()

    object UnknownException : OfferException()

    data class ValidationException(override val message: String) : OfferException(message)
}