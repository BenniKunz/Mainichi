package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val database: AppDatabase

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

        val settings = mutableListOf<SettingsContract.UiState.Setting>()
        enumValues<SettingsContract.UiState.Settings>().forEach { setting ->
            settings.add(
                SettingsContract.UiState.Setting(
                    title = setting.toString(),
                    target = NavigateToShowNotificationsScreen
                )
            )
        }

        _uiState.update { uiState ->
            uiState.copy(
                settings = settings,
                isLoading = false)
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
                }
            }
    }
}