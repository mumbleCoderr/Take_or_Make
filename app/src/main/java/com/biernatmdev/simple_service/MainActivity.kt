package com.biernatmdev.simple_service

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.biernatmdev.simple_service.core.nav.SimpleServiceNavGraph
import com.biernatmdev.simple_service.core.ui.theme.SimpleServiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleServiceTheme {
                SimpleServiceNavGraph()
            }
        }
    }
}
