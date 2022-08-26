package com.example.mainichi.ui.settingsScreen

import com.example.mainichi.ui.settingsScreen.SettingsContract.NotificationConfiguration.*

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val notificationConfiguration: NotificationConfiguration,
//        val notifications: List<AssetNotification> = emptyList(), // own screen
    )

    //reuse in different screens // edit, create, show
    data class NotificationConfiguration(
        val assets: List<SelectableAsset> = emptyList(),
//        val notifyIncrease: Boolean = false,
//        val notifyDecrease: Boolean = false,

        val priceEvent: PriceEvent = PriceEvent.None,
        val eventValue: String = "",
        val anyEventValue: Boolean = false,

        val notificationInterval: String = "",
        val intervalPeriod: Periodically = Periodically.Hourly
    ) {
        data class SelectableAsset(
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

    sealed class SettingsEvent {

        object CreateCustomNotification : SettingsEvent()

        data class SelectAsset(
            val selectedAsset: SelectableAsset
        ) : SettingsEvent()

        data class SelectIntervalPeriod(val period: Periodically) : SettingsEvent()
        data class ChangeIntervalValue(val intervalValue : String) : SettingsEvent()
        data class ChangeEventValue(val eventValue : String) : SettingsEvent()
        data class ChangePriceEvent(val priceEvent : PriceEvent) : SettingsEvent()
        object ToggleAnyValue : SettingsEvent()

    }

    sealed class SettingsEffect {

    }
}

