package com.example.mainichi.ui.createNotificationScreen

import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.NotificationConfiguration.*

class CreateNotificationContract {

    data class UiState(
        val isLoading: Boolean,
        val notificationConfiguration: NotificationConfiguration,
//        val notifications: List<AssetNotification> = emptyList(), // own screen
    )

    //reuse in different screens // edit, create, show
    data class NotificationConfiguration(
        val assets: List<NotificationAsset> = emptyList(),
//        val notifyIncrease: Boolean = false,
//        val notifyDecrease: Boolean = false,

        val eventType: PriceEvent = PriceEvent.None,
        val eventValue: String = "",
        val anyEventValue: Boolean = false,

        val intervalValue: String = "",
        val intervalType: Periodically = Periodically.Hourly,

        val date: String = ""
    ) {
        data class NotificationAsset(
            val name: String,
            val symbol: String,
            val image: String,
            val selected: Boolean = false,
        )

        enum class Periodically {
            Hourly, Daily
        }

        enum class PriceEvent {
            None,
            PriceUp,
            PriceDown,
            PriceUpDown
        }
    }

    sealed class CreateNotificationEvent {

        object CreateCustomNotification : CreateNotificationEvent()

        data class SelectAsset(
            val selectedAsset: NotificationAsset
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

