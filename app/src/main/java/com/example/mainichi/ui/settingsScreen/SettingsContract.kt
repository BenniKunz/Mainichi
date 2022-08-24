package com.example.mainichi.ui.settingsScreen

import com.example.mainichi.ui.entities.Asset
import java.util.concurrent.TimeUnit

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val assets: List<Asset> = emptyList(),
        val notifications: List<AssetNotification> = emptyList(),
        val notificationParameters: NotificationParameters
    )

     sealed class SettingsEvent {

         data class CreateCustomNotification(
             val asset : String,
             val eventType : String,
             val eventValue: String,
             val repeatIntervalTimeUnit: TimeUnit,
             val repeatInterval: Long
         ) : SettingsEvent()
     }

     sealed class SettingsEffect {

     }
}

