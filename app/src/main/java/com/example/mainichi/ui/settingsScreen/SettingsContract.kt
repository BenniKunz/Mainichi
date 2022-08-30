package com.example.mainichi.ui.settingsScreen

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val settings: List<Setting> = emptyList()

    ) {
        data class Setting(
            val title: String,
            val target : SettingsEvent)

        enum class Settings{
            Notifications,
            Language,
            LaunchScreen
        }

    }

    sealed class SettingsEvent {

        object NavigateToShowNotificationsScreen : SettingsEvent()

    }

    sealed class SettingsEffect {

        object NavigateToShowNotificationsScreen : SettingsEffect()
    }
}

