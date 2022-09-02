package com.example.mainichi

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.mainichi.ui.settingsScreen.SettingsContract
import com.example.mainichi.ui.settingsScreen.themeDialog.ThemeDialogContract
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

        Log.d(
            "Theme setting", "I am initialized"
        )

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
                    state.copy(theme = ThemeDialogContract.UiState.Theme.values()[dataStoreValue])

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
                    state.copy(launchScreen = SettingsContract.UiState.LaunchScreen.values()[dataStoreValue])

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

                        ThemeDialogContract.UiState.Theme.values()[dataStore.data.map { preferences -> preferences[PreferenceKeys.theme] }
                            .first() ?: 2]
                    }
                    else -> {
                        settingsState.theme
                    }
                },
                launchScreen = when {
                    dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
                        .firstOrNull() != null -> {

                        SettingsContract.UiState.LaunchScreen.values()[dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
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

    data class SettingsState(
        val theme: ThemeDialogContract.UiState.Theme = ThemeDialogContract.UiState.Theme.SystemSetting,
        val launchScreen: SettingsContract.UiState.LaunchScreen = SettingsContract.UiState.LaunchScreen.Crypto
    )
}