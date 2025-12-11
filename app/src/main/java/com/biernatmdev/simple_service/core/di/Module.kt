package com.biernatmdev.simple_service.core.di

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.user.domain.UserRepository
import com.biernatmdev.simple_service.core.user.data.repository.UserRepositoryImpl
import com.biernatmdev.simple_service.features.auth.presentation.AuthViewModel
import com.biernatmdev.simple_service.features.home.presentation.HomeViewModel
import com.biernatmdev.simple_service.features.main.MainViewModel
import com.biernatmdev.simple_service.features.profile.presentation.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<UserRepository> { UserRepositoryImpl() }
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    //TODO offerrepository
    viewModel { HomeViewModel() }
    viewModel { ProfileViewModel(get()) }
    single { GoogleUiClient(
        context = androidContext(),
        auth = get(),
        serverClientId = androidContext().getString(R.string.default_web_client_id)
    ) }
}