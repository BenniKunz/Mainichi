package com.example.mainichi.ui.settingsScreen

import com.example.mainichi.ui.settingsScreen.SettingsContract.NotificationConfiguration.*

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val notificationConfiguration: NotificationConfiguration? = null,
//        val notifications: List<AssetNotification> = emptyList(), // own screen
    )

    //reuse in different screens // edit, create, show
    data class NotificationConfiguration(
        val assets: List<SelectableAsset>,
        val notifyIncrease: Boolean = false,
        val notifyDecrease: Boolean = false,

        val priceEvent: PriceEvent = PriceEvent.PriceUp,
        val eventValue: Int = 5,
        val anyEventValue: Boolean = true,

        val notificationInterval: Int = 1,
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
            PriceUp,
            PriceDown
        }
    }

    sealed class SettingsEvent {

        data class CreateCustomNotification(val asset: String) : SettingsEvent()

        data class SelectAsset(
            val selectedAsset: SelectableAsset
        ) : SettingsEvent()

        data class SelectIntervalPeriod(val period: Periodically) : SettingsEvent()

    }

    sealed class SettingsEffect {

    }
}

