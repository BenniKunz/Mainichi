package com.example.mainichi.ui.settingsScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsContract.UiState> =
        MutableStateFlow(SettingsContract.UiState(isLoading = true))
    val uiState: StateFlow<SettingsContract.UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<SettingsContract.SettingsEvent> = MutableSharedFlow()

    fun setEvent(event: SettingsContract.SettingsEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<SettingsContract.SettingsEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> SettingsContract.SettingsEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {

        viewModelScope.launch {


        }

        viewModelScope.launch {

            handleEvents()
        }
    }

    private suspend fun handleEvents() {
        _event
            .debounce(timeoutMillis = 300)
            .collect { event ->

                when (event) {

                    else -> {}
                }
            }
    }
}