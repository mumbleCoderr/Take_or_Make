package com.biernatmdev.simple_service.core.user.domain.model

sealed class UserException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {
    object WrongCredentials : UserException()
    object TooManyRequests : UserException()
    object NotFoundException : UserException()
    object EmailAlreadyInUse : UserException()
    object InvalidEmailFormat : UserException()
    object WeakPassword : UserException()
    object NetworkError : UserException()
    object AccessDeniedException : UserException()
    object NotSignedInException : UserException()
    object UnknownException : UserException()
    data class ValidationException(override val message: String) : UserException(message)
}