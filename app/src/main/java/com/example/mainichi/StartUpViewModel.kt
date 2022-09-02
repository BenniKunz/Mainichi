package com.example.mainichi

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.LaunchScreen
import com.example.mainichi.ui.settingsScreen.themeDialog.ThemeDialogContract.UiState.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartUpViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val settingsState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())

    init {

        viewModelScope.launch {

            updateState()
        }

        Log.d(
            "Theme setting", "I am initialized"
        )

        viewModelScope.launch {

            collectThemeChanges()
        }
    }

    private suspend fun collectThemeChanges() {
        dataStore.data.map { preferences -> preferences[PreferenceKeys.theme] }
            .collect { dataStoreValue ->

                if (dataStoreValue == null || dataStoreValue == settingsState.value.theme.ordinal) {
                    return@collect
                }
                settingsState.update { state ->
                    state.copy(theme = Theme.values()[dataStoreValue])

                }
            }
    }

    private suspend fun updateState() {
        settingsState.update { settingsState ->

            settingsState.copy(
                theme =
                when {
                    dataStore.data.map { preferences -> preferences[PreferenceKeys.theme] }
                        .firstOrNull() != null -> {

                        Theme.values()[dataStore.data.map { preferences -> preferences[PreferenceKeys.theme] }
                            .first() ?: 2]
                    }
                    else -> {
                        settingsState.theme
                    }
                },
                launchScreen = when {
                    dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
                        .firstOrNull() != null -> {

                        LaunchScreen.values()[dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
                            .first() ?: 0]
                    }
                    else -> {
                        settingsState.launchScreen
                    }
                }

//                LaunchScreen.values()[dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
//                    .first() ?: 0]
            )
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

