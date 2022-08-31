package com.example.mainichi.ui.settingsScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mainichi.ui.settingsScreen.SettingsContract.*
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.*

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val isDarkMode: Boolean = false
    ) {

        enum class Setting {
            Notifications,
            Language,
            LaunchScreen,
            ToggleDarkMode
        }

        enum class LaunchScreen {
            Crypto,
            News
        }
    }

    sealed class SettingsEvent {

        object NavigateToShowNotificationsScreen : SettingsEvent()
        object ToggleDarkMode : SettingsEvent()
        object ChangeLaunchScreen : SettingsEvent()

    }

    sealed class SettingsEffect {

        object NavigateToShowNotificationsScreen : SettingsEffect()
    }
}

fun Setting.getImageVector(isDarkMode: Boolean = false): ImageVector {

    return when (this) {
        Setting.Notifications -> Icons.Default.ChevronRight
        Setting.Language -> Icons.Default.ChevronRight
        Setting.LaunchScreen -> Icons.Default.ChevronRight
        Setting.ToggleDarkMode -> if (isDarkMode) {
            Icons.Default.DarkMode
        } else {
            Icons.Default.LightMode
        }
    }
}

    fun Setting.getEvent(): SettingsEvent {
        return when (this) {
            Setting.Notifications -> SettingsEvent.NavigateToShowNotificationsScreen
            Setting.Language -> SettingsEvent.NavigateToShowNotificationsScreen
            Setting.LaunchScreen -> SettingsEvent.ChangeLaunchScreen
            Setting.ToggleDarkMode -> SettingsEvent.ToggleDarkMode
        }
    }

    fun Setting.asString(): String {
        val string = this.toString()

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

