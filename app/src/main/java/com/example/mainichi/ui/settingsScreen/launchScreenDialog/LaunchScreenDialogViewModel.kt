package com.example.mainichi.ui.settingsScreen.launchScreenDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.PreferenceKeys
import com.example.mainichi.Settings
import com.example.mainichi.ui.settingsScreen.SettingsContract
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.LaunchScreen
import com.example.mainichi.ui.settingsScreen.launchScreenDialog.LaunchScreenDialogContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LaunchScreenDialogViewModel @Inject constructor(
    private val settings: Settings
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true
            )
        )

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<LaunchScreenDialogEvent> = MutableSharedFlow()

    fun setEvent(event: LaunchScreenDialogEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<LaunchScreenDialogEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> LaunchScreenDialogEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {

        viewModelScope.launch {

            settings.settingsState.collect { settingsState ->

                _uiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        currentScreen = settingsState.launchScreen
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
                    is LaunchScreenDialogEvent.SetLaunchScreen -> {

                        settings.updateDataStore(
                            PreferenceKeys.launchScreen,
                            event.launchScreen.ordinal
                        )
                    }
                }
            }
    }
}