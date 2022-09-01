package com.example.mainichi.ui.settingsScreen.launchScreenDialog

import com.example.mainichi.ui.settingsScreen.SettingsContract

class LaunchScreenDialogContract {

    data class UiState(
        val isLoading: Boolean,
        val isCrypto: Boolean = false,
        val isNews: Boolean = false

    )

    sealed class LaunchScreenDialogEvent {

        data class SetLaunchScreen(val launchScreen: SettingsContract.UiState.LaunchScreen) : LaunchScreenDialogEvent()

    }

    sealed class LaunchScreenDialogEffect {


    }
}
