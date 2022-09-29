package com.bknz.mainichi.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.bknz.mainichi.core.model.LaunchScreen
import com.bknz.mainichi.core.model.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Settings @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    val settingsState: MutableStateFlow<SettingsState> = MutableStateFlow(
        SettingsState()
    )

    init {

        scope.launch {

            updateState()
        }

        scope.launch {

            collectThemeChanges()
        }

        scope.launch {
            collectLaunchScreenChanges()
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

    private suspend fun collectLaunchScreenChanges() {
        dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
            .collect { dataStoreValue ->

                if (dataStoreValue == null || dataStoreValue == settingsState.value.launchScreen.ordinal) {
                    return@collect
                }
                settingsState.update { state ->
                    state.copy(launchScreen = LaunchScreen.values()[dataStoreValue])

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
                })

        }
    }

    suspend fun updateDataStore(preferenceKey: Preferences.Key<Int>, value: Int) {

        dataStore.edit { settings ->
            settings[preferenceKey] = value
        }
    }

    suspend fun getDataStoreValue(preferenceKey: Preferences.Key<Int>): Int {

        return dataStore.data.map { preferences -> preferences[preferenceKey] }
            .first() ?: 0
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