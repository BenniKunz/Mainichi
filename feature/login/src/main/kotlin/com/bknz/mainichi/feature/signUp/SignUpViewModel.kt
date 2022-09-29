package com.bknz.mainichi.feature.signUp

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

internal data class UiState(
    val loading: Boolean = true,
    val signedUp: Boolean = false,
)

internal sealed class SignUpEffect {

    data class Navigate(val route: String) : SignUpEffect()
}

internal sealed class SignUpEvent {

    data class Navigate(val route: String) : SignUpEvent()

}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settings: Settings
) : ViewModel() {

    private val _event: MutableSharedFlow<SignUpEvent> = MutableSharedFlow()

    fun setEvent(event: SignUpEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<SignUpEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> SignUpEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {
            _event.collect { event ->

                when (event) {
                    is SignUpEvent.Navigate -> setEffect {
                        SignUpEffect.Navigate(
                            event.route
                        )
                    }
                }
            }
        }
    }

    val uiState = userRepository.userData.mapLatest { userData ->

        UiState(
            loading = false
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UiState()
    )

    fun signUpWithEmail(email: String, password: String) {

        userRepository.createEmailAccount(email = email, password = password) { exception ->

            when (exception) {
                null -> setEffect {
                    SignUpEffect.Navigate(
                        when (settings.settingsState.value.launchScreen) {
                            LaunchScreen.Crypto -> "crypto"
                            LaunchScreen.News -> "news"
                        }
                    )
                }
                else -> {
                    Log.d("Auth Test", "Couldn't authenticate")
                }
            }
        }
    }
}