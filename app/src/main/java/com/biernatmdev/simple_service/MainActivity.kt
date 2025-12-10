package com.biernatmdev.simple_service

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.biernatmdev.simple_service.core.nav.SimpleServiceNavGraph
import com.biernatmdev.simple_service.core.ui.theme.SimpleServiceTheme
import com.biernatmdev.simple_service.features.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.state.value.isLoading
        }
        enableEdgeToEdge()
        setContent {
            SimpleServiceTheme {
                val startDestination = mainViewModel.state.collectAsState().value.startDestination
                SimpleServiceNavGraph(startDestination = startDestination)
            }
        }
    }
}
