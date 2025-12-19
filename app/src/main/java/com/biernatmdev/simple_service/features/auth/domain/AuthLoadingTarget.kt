package com.biernatmdev.simple_service.features.auth.domain

enum class AuthLoadingTarget {
    NONE,
    SIGN_UP_EMAIL,
    SIGN_IN_EMAIL,
    SIGN_IN_GOOGLE,
    SIGN_IN_GUEST
}