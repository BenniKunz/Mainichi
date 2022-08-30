package com.example.mainichi.ui.showNotificationScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.db.AppDatabase
import com.example.mainichi.db.toNotificationConfiguration
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ShowNotificationViewModel @Inject constructor(
    val database: AppDatabase

) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true
            )
        )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<ShowNotificationEvent> = MutableSharedFlow()

    fun setEvent(event: ShowNotificationEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<ShowNotificationEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> ShowNotificationEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }


    init {

        viewModelScope.launch {

            loadData()

            handleEvents()
        }
    }

    private suspend fun loadData() {

        database.notificationsDao().getAllNotifications().collect{ dbNotifications ->

            _uiState.update { uiState ->

                uiState.copy(
                    isLoading = false,
                    notificationConfigurations = dbNotifications.map { dbNotification ->
                        dbNotification.toNotificationConfiguration()
                    }
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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