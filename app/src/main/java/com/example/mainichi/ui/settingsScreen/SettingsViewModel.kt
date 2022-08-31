package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.PreferenceKeys
import com.example.StartUpViewModel
import com.example.mainichi.db.AppDatabase
import com.example.mainichi.db.toNotificationConfiguration
import com.example.mainichi.ui.settingsScreen.SettingsContract.*
import com.example.mainichi.ui.settingsScreen.SettingsContract.SettingsEvent.*
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract.*
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    val database: AppDatabase,
    private val dataStore: DataStore<Preferences>

) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsContract.UiState> =
        MutableStateFlow(
            SettingsContract.UiState(
                isLoading = true
            )
        )
    val uiState: StateFlow<SettingsContract.UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<SettingsEvent> = MutableSharedFlow()

    fun setEvent(event: SettingsEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<SettingsEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> SettingsEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }


    init {

        viewModelScope.launch {

            loadData()

        }

        viewModelScope.launch {
            handleEvents()
        }
    }

    private suspend fun loadData() {

        _uiState.update { uiState ->
            uiState.copy(
                isLoading = false,
                isDarkMode = dataStore.data.map { preferences -> preferences[PreferenceKeys.isDarkMode] }
                    .first() ?: false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleEvents() {
        _event
            .debounce(timeoutMillis = 300)
            .collect { event ->

                when (event) {
                    NavigateToShowNotificationsScreen -> {
                        setEffect {
                            SettingsEffect.NavigateToShowNotificationsScreen
                        }
                    }
                    ToggleDarkMode -> {

                        val isDarkMode =
                            dataStore.data.map { preferences -> preferences[PreferenceKeys.isDarkMode] }
                                .first() ?: false

                        dataStore.edit { settings ->
                            settings[PreferenceKeys.isDarkMode] = !isDarkMode
                        }

                        _uiState.update { uiState ->
                            uiState.copy(isDarkMode = !isDarkMode)
                        }
                    }
                    ChangeLaunchScreen -> {

                        dataStore.edit { settings ->
                            settings[PreferenceKeys.launchScreen] =
                                if (dataStore.data.map { preferences -> preferences[PreferenceKeys.launchScreen] }
                                        .first() == 0) {
                                    1
                                } else {
                                    0
                                }
                        }
                    }
                }
            }
    }
}