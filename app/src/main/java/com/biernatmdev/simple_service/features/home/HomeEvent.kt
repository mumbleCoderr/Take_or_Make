package com.biernatmdev.simple_service.features.home

import com.biernatmdev.simple_service.features.home.domain.HomeMode

sealed interface HomeEvent {
    data class ChangeMode(val mode: HomeMode) : HomeEvent
}