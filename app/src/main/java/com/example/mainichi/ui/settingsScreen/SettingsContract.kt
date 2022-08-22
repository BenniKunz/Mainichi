package com.example.mainichi.ui.settingsScreen

 class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
    )

     sealed class SettingsEvent {


     }

     sealed class SettingsEffect {

     }
}

