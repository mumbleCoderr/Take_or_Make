package com.biernatmdev.simple_service.features.home.presentation

import com.biernatmdev.simple_service.features.home.domain.HomeMode

data class HomeState(
    val mode: HomeMode = HomeMode.TAKE
)