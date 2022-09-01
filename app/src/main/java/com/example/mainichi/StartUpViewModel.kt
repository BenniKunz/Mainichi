package com.example.mainichi

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.*
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

            setInitialSettings()
        }

        viewModelScope.launch {

            updateState()
        }

        Log.d(
            "DataStore", "I am initialized"
        )

        viewModelScope.launch {

            collectThemeChanges()
        }

        viewModelScope.launch {
            collectLaunchScreenChanges()
        }
    }

    private suspend fun collectLaunchScreenChanges() {
        dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
            .collect { dataStoreValue ->

                settingsState.update { state ->
                    state.copy(launchScreen = LaunchScreen.values()[dataStoreValue ?: 0])

                }
            }
    }

    private suspend fun collectThemeChanges() {
        dataStore.data.map { preferences -> preferences[PreferenceKeys.isDarkMode] }
            .collect { dataStoreValue ->

                settingsState.update { state ->
                    state.copy(isDarkMode = dataStoreValue ?: false)

                }
            }
    }

    private suspend fun updateState() {
        settingsState.update {
            SettingsState(
                isDarkMode = dataStore.data.map { preferences -> preferences[PreferenceKeys.isDarkMode] }
                    .first() ?: true,
                launchScreen = LaunchScreen.values()[dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
                    .first() ?: 0]
            )
        }
    }

    private suspend fun setInitialSettings() {
        if (dataStore.data.map { preferences -> preferences[PreferenceKeys.isDarkMode] }
                .firstOrNull() == null) {
            dataStore.edit { settings ->
                settings[PreferenceKeys.isDarkMode] = true
            }
        }

        if (dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
                .firstOrNull() == null) {
            dataStore.edit { settings ->
                settings[PreferenceKeys.launchScreen] = 0
            }
        }
    }


    data class SettingsState(
        val isDarkMode: Boolean = true,
        val launchScreen: LaunchScreen = LaunchScreen.Crypto
    )
}

object PreferenceKeys {
    val isDarkMode = booleanPreferencesKey(UserSettings.ISDARKMODE.toString())
    val launchScreen = intPreferencesKey(UserSettings.LAUNCHSCREEN.toString())
}

enum class UserSettings {
    ISDARKMODE,
    LAUNCHSCREEN
}

