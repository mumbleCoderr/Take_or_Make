package com.biernatmdev.simple_service.core.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Forward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Wallet
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
        val Google = R.drawable.google

        // BOTTOM BAR
        val Home = Icons.Filled.Home
        val Category = Icons.Filled.Category
        val Notification = Icons.Filled.Notifications
        val Profile = R.drawable.profile

        // TOP BAR
        val Take = R.drawable.take
        val Make = R.drawable.make

        // PROFILE OPTION
        val Forward = Icons.AutoMirrored.Filled.ArrowForwardIos
        val Details = Icons.Filled.ManageAccounts
        val Wallet = Icons.Filled.Wallet
        val Pro = Icons.Filled.AttachMoney
        val Statistics = R.drawable.finance
        val History = Icons.Filled.History
        val Reviews = Icons.Filled.Reviews
        val Favourites = Icons.Filled.FavoriteBorder
    }

    object Image {
        val Logo_v1 = R.drawable.simple_service_logo_v1
        val Logo_v2 = R.drawable.simple_service_logo_v2
        val Logo_v1_dark = R.drawable.simple_service_logo_v1_dark
        val Profile_picture_placeholder = R.drawable.profile_picture_placeholder
        val Profile_picture_background = R.drawable.profile_picture_background
    }
}