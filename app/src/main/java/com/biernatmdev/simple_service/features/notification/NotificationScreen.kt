package com.biernatmdev.simple_service.features.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.biernatmdev.simple_service.features.home.make_module.presentation.offer_list.EmptyListComponent

@Composable
fun NotificationScreen(){
    Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ){
        EmptyListComponent(
            isNotificationScreenTmp = true,
            isOfferListEmpty = true,
            isActiveFilter = false,
            isLoading = false,
        )
    }

}