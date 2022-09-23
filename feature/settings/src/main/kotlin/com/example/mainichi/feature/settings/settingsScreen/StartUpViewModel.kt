package com.example.mainichi.feature.settings.settingsScreen

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.feature.settings.settingsScreen.SettingsContract.UiState.LaunchScreen
import com.example.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogContract.UiState.Theme
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