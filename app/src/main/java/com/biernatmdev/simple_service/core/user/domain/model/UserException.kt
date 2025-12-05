package com.biernatmdev.simple_service.core.user.domain.model

sealed class UserException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {
    data object NotSignedInException: UserException()
    data object NotFoundException: UserException()
    data object AccessDeniedException: UserException()
    data class ValidationException(override val message: String) : UserException(message)
}