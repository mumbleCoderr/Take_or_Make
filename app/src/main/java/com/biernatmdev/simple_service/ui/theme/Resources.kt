package com.biernatmdev.simple_service.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Dvr
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Rtt
import androidx.compose.material.icons.automirrored.filled.Toc
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Sell
import com.biernatmdev.simple_service.R

object Resources {

    object Icon {

        // SPLASH SCREEN
        val Handshake = Icons.Filled.Handshake

        // AUTH SCREEN
        val Construction = Icons.Outlined.Construction
        val Campaign = Icons.Outlined.Campaign
        val Sell = Icons.Outlined.Sell
        val Approval = R.drawable.approval

        // BUTTONS
        val LogIn = Icons.AutoMirrored.Filled.Login
        val Google = R.drawable.googleicon

        // BOTTOM BAR
        val Home = Icons.Outlined.Home
        val Category = Icons.Outlined.Category
        val Notification = Icons.Outlined.Notifications
        val Profile = Icons.Outlined.AccountCircle
    }

    object Image {
        val Logo_v1 = R.drawable.simple_service_logo_v1
        val Logo_v2 = R.drawable.simple_service_logo_v2
        val Logo_v1_dark = R.drawable.simple_service_logo_v1_dark
    }
}