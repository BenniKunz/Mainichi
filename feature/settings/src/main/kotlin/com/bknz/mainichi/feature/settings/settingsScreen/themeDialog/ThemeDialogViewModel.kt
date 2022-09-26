package com.bknz.mainichi.feature.settings.settingsScreen.themeDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bknz.mainichi.feature.settings.settingsScreen.PreferenceKeys
import com.bknz.mainichi.feature.settings.settingsScreen.Settings
import com.bknz.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogContract.*
import com.bknz.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogContract.ThemeDialogEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ThemeDialogViewModel @Inject constructor(
    private val settings: Settings
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true
            )
        )

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<ThemeDialogEvent> = MutableSharedFlow()

    fun setEvent(event: ThemeDialogEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<ThemeDialogEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> ThemeDialogEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {

        viewModelScope.launch {

            settings.settingsState.collect { settingsState ->

                _uiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        currentTheme = settingsState.theme
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

                    is SetTheme -> {
                        settings.updateDataStore(PreferenceKeys.theme, event.theme.ordinal)
                    }
                }
            }
    }
}