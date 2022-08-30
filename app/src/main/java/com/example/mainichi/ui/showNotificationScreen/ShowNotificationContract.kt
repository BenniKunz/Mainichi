package com.example.mainichi.ui.showNotificationScreen

import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.*

class ShowNotificationContract {

    data class UiState(
        val isLoading: Boolean,
        val notificationConfigurations: List<NotificationConfiguration> = emptyList(),
    )

    sealed class ShowNotificationEvent {


    }

    sealed class ShowNotificationEffect {

    }
}

