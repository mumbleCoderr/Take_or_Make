package com.biernatmdev.simple_service.core.di

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.data.auth.GoogleUiClient
import com.biernatmdev.simple_service.core.data.domain.UserRepository
import com.biernatmdev.simple_service.core.data.repoImpl.UserRepositoryImpl
import com.biernatmdev.simple_service.features.auth.AuthViewModel
import com.biernatmdev.simple_service.features.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<UserRepository> { UserRepositoryImpl() }
    viewModel { AuthViewModel(get(), auth = get()) }
    //TODO offerrepository
    viewModel { HomeViewModel() }
    single { GoogleUiClient(
        context = androidContext(),
        auth = get(),
        serverClientId = androidContext().getString(R.string.default_web_client_id)
    ) }
}