package com.example.mainichi

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.LaunchScreen
import com.example.mainichi.ui.settingsScreen.asText
import com.example.mainichi.ui.settingsScreen.themeDialog.ThemeDialogContract.UiState.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartUpViewModel @Inject constructor(
    private val settings: Settings
) : ViewModel() {

    val settingsState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())

    init {

        viewModelScope.launch {

            settingsState.update { settingsState ->

                settingsState.copy(
                    launchScreen = LaunchScreen.values()[settings.getDataStoreValue(PreferenceKeys.launchScreen)]
                )
            }

            settings.settingsState.collect { state ->

                settingsState.update { settingsState ->
                    settingsState.copy(
                        theme = state.theme
                    )
                }
            }
        }
    }

    data class SettingsState(
        val theme: Theme = Theme.SystemSetting,
        val launchScreen: LaunchScreen = LaunchScreen.Crypto
    )
}

object PreferenceKeys {
    val theme = intPreferencesKey(UserSettings.THEME.toString())
    val launchScreen = intPreferencesKey(UserSettings.LAUNCHSCREEN.toString())
}

enum class UserSettings {
    THEME,
    LAUNCHSCREEN
}