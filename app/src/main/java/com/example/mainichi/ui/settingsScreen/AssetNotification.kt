package com.example.mainichi.ui.settingsScreen

data class AssetNotification(
    val name: String,
    val image: String,
    val symbol: String,
    val event: String,
    val eventValue: String,
    val intervalType: SettingsContract.NotificationConfiguration.Periodically,
    val interval: String,
    val date: String
)