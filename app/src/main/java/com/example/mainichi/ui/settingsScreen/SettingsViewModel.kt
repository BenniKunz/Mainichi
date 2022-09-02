package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.PreferenceKeys
import com.example.mainichi.Settings
import com.example.mainichi.db.AppDatabase
import com.example.mainichi.ui.settingsScreen.SettingsContract.*
import com.example.mainichi.ui.settingsScreen.SettingsContract.SettingsEvent.*
import com.example.mainichi.ui.settingsScreen.themeDialog.ThemeDialogContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    val database: AppDatabase,
    private val settings: Settings

) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true
            )
        )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

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

            settings.settingsState.collect { settingsState ->

                _uiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        currentTheme = settingsState.theme,
                        currentLaunchScreen = settingsState.launchScreen
                    )
                }
            }
        }

        viewModelScope.launch {
            handleEvents()
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
                    is SetTheme -> {

                        _uiState.update { uiState ->
                            uiState.copy(
                                setTheme = !uiState.setTheme
                            )
                        }
                    }
                    ChangeLaunchScreen -> {

                        _uiState.update { uiState ->

                            uiState.copy(setLaunchScreen = !uiState.setLaunchScreen)
                        }
                    }
                }
            }
    }
}