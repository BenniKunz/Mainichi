package com.bknz.mainichi.ui.settingsScreen.launchScreenDialog

import com.bknz.mainichi.feature.settings.settingsScreen.SettingsContract

class LaunchScreenDialogContract {

    data class UiState(
        val isLoading: Boolean,
        val currentScreen: SettingsContract.UiState.LaunchScreen? = null

    )

    sealed class LaunchScreenDialogEvent {

        data class SetLaunchScreen(val launchScreen: SettingsContract.UiState.LaunchScreen) : LaunchScreenDialogEvent()

    }

    sealed class LaunchScreenDialogEffect {


    }
}
