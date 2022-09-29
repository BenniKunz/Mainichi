package com.bknz.mainichi.feature.settings.settingsScreen.themeDialog

import com.bknz.mainichi.core.model.Theme

class ThemeDialogContract {

    data class UiState(
        val isLoading: Boolean,
        val currentTheme : Theme? = null,
    )

    sealed class ThemeDialogEvent {

        data class SetTheme(val theme: Theme) : ThemeDialogEvent()

    }

    sealed class ThemeDialogEffect {


    }
}
