package com.bknz.mainichi.feature.notification.createNotificationScreen

import com.bknz.mainichi.core.model.Asset
import com.bknz.mainichi.core.model.NotificationConfiguration
import com.bknz.mainichi.core.model.NotificationConfiguration.PriceEvent
import com.bknz.mainichi.core.model.NotificationConfiguration.Periodically

class CreateNotificationContract {

    data class UiState(
        val isLoading: Boolean,
        val notificationConfiguration: NotificationConfiguration,
//        val notifications: List<AssetNotification> = emptyList(), // own screen
    )

    sealed class CreateNotificationEvent {

        object CreateCustomNotification : CreateNotificationEvent()

        data class SelectAsset(
            val selectedAsset: Asset
        ) : CreateNotificationEvent()

        data class SelectIntervalPeriod(val period: Periodically) : CreateNotificationEvent()
        data class ChangeIntervalValue(val intervalValue: String) : CreateNotificationEvent()
        data class ChangeEventValue(val eventValue: String) : CreateNotificationEvent()
        data class ChangePriceEvent(val priceEvent: PriceEvent) : CreateNotificationEvent()
        object ToggleAnyValue : CreateNotificationEvent()

    }

    sealed class CreateNotificationEffect {

    }
}

fun Periodically.asString() : String {
    return when(this) {
        Periodically.Hourly -> "hours"
        Periodically.Daily -> "days"
    }
}