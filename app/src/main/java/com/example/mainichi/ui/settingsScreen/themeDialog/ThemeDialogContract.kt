package com.example.mainichi.ui.settingsScreen.themeDialog

import com.example.mainichi.ui.settingsScreen.themeDialog.ThemeDialogContract.UiState.Theme

class ThemeDialogContract {

    data class UiState(
        val isLoading: Boolean,
        val currentTheme : Theme? = null,
    ) {
        enum class Theme {
            DarkMode,
            LightMode,
            SystemSetting
        }
    }

    sealed class ThemeDialogEvent {

        data class SetTheme(val theme: Theme) : ThemeDialogEvent()

    }

    sealed class ThemeDialogEffect {


    }
}
