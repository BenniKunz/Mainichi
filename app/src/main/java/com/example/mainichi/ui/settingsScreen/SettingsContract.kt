package com.example.mainichi.ui.settingsScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mainichi.ui.settingsScreen.SettingsContract.*
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.*

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val isDarkMode: Boolean = false,
        val setLaunchScreen: Boolean = false
    ) {

        enum class Setting {
            Notifications,
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
        Setting.Notifications -> Icons.Default.Notifications
        Setting.LaunchScreen -> Icons.Default.Home
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

