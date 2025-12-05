package com.biernatmdev.simple_service.core.user.domain.model

sealed class UserException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {
    data object NotSignedInException: UserException("User not signed in")
    data object NotFoundException: UserException("User not found")
    data object AccessDeniedException: UserException("You are not permitted to do that")
    data class ValidationException(override val message: String) : UserException(message)
    data class UnknownException(val originalException: Throwable?) :
        UserException("External error: ${originalException?.message}", originalException)
}