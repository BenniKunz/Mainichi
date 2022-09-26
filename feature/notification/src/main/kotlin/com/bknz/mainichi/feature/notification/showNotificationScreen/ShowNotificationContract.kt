package com.bknz.mainichi.feature.notification.showNotificationScreen

import com.bknz.mainichi.core.model.NotificationConfiguration

class ShowNotificationContract {

    data class UiState(
        val isLoading: Boolean,
        val notificationConfigurations: List<NotificationConfiguration> = emptyList(),
    )

    sealed class ShowNotificationEvent {

        object NavigateToCreateNotificationScreen : ShowNotificationEvent()

    }

    sealed class ShowNotificationEffect {

        object NavigateToCreateNotificationScreen : ShowNotificationEffect()
    }
}

