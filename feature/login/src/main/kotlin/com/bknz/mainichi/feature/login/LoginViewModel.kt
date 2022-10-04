package com.bknz.mainichi.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bknz.mainichi.core.model.LaunchScreen
import com.bknz.mainichi.data.Settings
import com.bknz.mainichi.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class LoginUiState(
    val loading: Boolean = true,
    val authenticatedAnonymously: Boolean = false,
    val authenticatedCredentials: Boolean = false
)

internal sealed class LoginEffect {

    data class Navigate(val route: String) : LoginEffect()
}

internal sealed class LoginEvent {

    data class Navigate(val route: String) : LoginEvent()

}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settings: Settings
) : ViewModel() {

    private val _event: MutableSharedFlow<LoginEvent> = MutableSharedFlow()

    fun setEvent(event: LoginEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<LoginEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> LoginEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {
            _event.collect { event ->

                when (event) {
                    is LoginEvent.Navigate -> setEffect {
                        LoginEffect.Navigate(
                            event.route
                        )
                    }
                }
            }
        }
    }

    val uiState = userRepository.userData.mapLatest { userData ->

        LoginUiState(
            loading = false,
            authenticatedAnonymously = userData.authenticatedAnonymously,
            authenticatedCredentials = userData.authenticatedWithEmail
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoginUiState()
    )

    fun loginAnonymously() {

        userRepository.createAnonymousAccount() { exception ->
            when (exception) {
                null -> {
                    setEffect {
                        LoginEffect.Navigate(
                            when (settings.settingsState.value.launchScreen) {
                                LaunchScreen.Crypto -> "crypto"
                                LaunchScreen.News -> "news"
                            }
                        )
                    }
                }
                else -> {
                    Log.d("Auth Test", "Couldn't authenticate: ${exception.message}")
                }
            }
        }
    }
}