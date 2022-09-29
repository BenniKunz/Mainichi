package com.bknz.mainichi.feature.settings.settingsScreen.launchScreenDialog

import com.bknz.mainichi.core.model.LaunchScreen

class LaunchScreenDialogContract {

    data class UiState(
        val isLoading: Boolean,
        val currentScreen: LaunchScreen? = null

    )

    sealed class LaunchScreenDialogEvent {

        data class SetLaunchScreen(val launchScreen: LaunchScreen) : LaunchScreenDialogEvent()

    }

    sealed class LaunchScreenDialogEffect {


    }
}
