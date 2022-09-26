package com.bknz.mainichi.feature.settings.settingsScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.bknz.mainichi.feature.settings.settingsScreen.SettingsContract.*
import com.bknz.mainichi.feature.settings.settingsScreen.SettingsContract.UiState.Setting
import com.bknz.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogContract.UiState.Theme

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val setLaunchScreen: Boolean = false,
        val setTheme: Boolean = false,
        val currentTheme: Theme = Theme.SystemSetting,
        val currentLaunchScreen: LaunchScreen = LaunchScreen.Crypto
    ) {

        enum class Setting {
            Notifications,
            LaunchScreen,
            Theme
        }

        enum class LaunchScreen {
            Crypto,
            News
        }
    }

    sealed class SettingsEvent {

        object NavigateToShowNotificationsScreen : SettingsEvent()
        object SetTheme : SettingsEvent()
        object ChangeLaunchScreen : SettingsEvent()

    }

    sealed class SettingsEffect {

        object NavigateToShowNotificationsScreen : SettingsEffect()
    }
}

fun Setting.getImageVector(theme: Theme = Theme.SystemSetting): ImageVector {

    return when (this) {
        Setting.Notifications -> Icons.Default.Notifications
        Setting.LaunchScreen -> Icons.Default.Home
        Setting.Theme -> when (theme) {
            Theme.DarkMode -> Icons.Default.DarkMode
            Theme.LightMode -> Icons.Default.LightMode
            Theme.SystemSetting -> Icons.Default.SettingsSuggest
        }

    }
}

fun Setting.getEvent(): SettingsEvent {
    return when (this) {
        Setting.Notifications -> SettingsEvent.NavigateToShowNotificationsScreen
        Setting.LaunchScreen -> SettingsEvent.ChangeLaunchScreen
        Setting.Theme -> SettingsEvent.SetTheme
    }
}

fun String.asText(): String {
    val string = this

    val indexList = mutableListOf<Int>()
    string.forEachIndexed() { index, char ->
        if (char.isUpperCase() && index != 0) {
            indexList.add(index)
        }
    }

    if (indexList.isEmpty()) {
        return string
    }

    var newString = ""

    indexList.forEachIndexed() { index, stringIndex ->

        if (indexList.size == 1) {
            newString += string.substring(0, stringIndex) + " " + string.substring(
                stringIndex,
                string.length
            )
        }

        if (indexList.size > 1) {
            when (index) {
                0 -> {

                    newString += string.substring(0, stringIndex) + " "

                }
                indexList.size - 1 -> {

                    newString += string.substring(
                        indexList[index - 1],
                        stringIndex
                    ) + " " + string.substring(stringIndex, string.length)
                }
                else -> {

                    newString += string.substring(indexList[index - 1], stringIndex) + " "
                }
            }
        }
    }
    return newString
}

