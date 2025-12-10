package com.biernatmdev.simple_service.features.main

sealed interface MainEvent {
    data object CheckAuth : MainEvent
}