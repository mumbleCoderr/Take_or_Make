package com.biernatmdev.simple_service.core.di

import com.biernatmdev.simple_service.R
import com.biernatmdev.simple_service.core.google_auth.GoogleUiClient
import com.biernatmdev.simple_service.core.offer.data.repository.OfferRepositoryImpl
import com.biernatmdev.simple_service.core.offer.domain.repository.OfferRepository
import com.biernatmdev.simple_service.core.user.domain.repository.UserRepository
import com.biernatmdev.simple_service.core.user.data.repository.UserRepositoryImpl
import com.biernatmdev.simple_service.features.auth.presentation.AuthViewModel
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.MakeViewModel
import com.biernatmdev.simple_service.features.home.make_module.presentation.wizard.AddOfferWizardViewModel
import com.biernatmdev.simple_service.features.user_details.presentation.UserDetailsViewModel
import com.biernatmdev.simple_service.features.home.presentation.HomeViewModel
import com.biernatmdev.simple_service.features.main.MainViewModel
import com.biernatmdev.simple_service.features.profile.presentation.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<OfferRepository> { OfferRepositoryImpl(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { HomeViewModel() }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserDetailsViewModel(get()) }
    viewModel { MakeViewModel() }
    viewModel { AddOfferWizardViewModel(get(), get()) }
    single { GoogleUiClient(
        context = androidContext(),
        auth = get(),
        serverClientId = androidContext().getString(R.string.default_web_client_id)
    ) }
}