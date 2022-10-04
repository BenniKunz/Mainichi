package com.bknz.mainichi.feature.signIn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bknz.mainichi.core.model.LaunchScreen
import com.bknz.mainichi.data.Settings
import com.bknz.mainichi.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class SignInUiState(
    val loading: Boolean = true,
    val signedIn: Boolean = false,
)

internal sealed class SignInEffect {

    data class Navigate(val route: String) : SignInEffect()
}

internal sealed class SignInEvent {

    data class Navigate(val route: String) : SignInEvent()

}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settings: Settings
) : ViewModel() {

    private val _event: MutableSharedFlow<SignInEvent> = MutableSharedFlow()

    fun setEvent(event: SignInEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<SignInEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> SignInEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {
            _event.collect { event ->

                when (event) {
                    is SignInEvent.Navigate -> setEffect {
                        SignInEffect.Navigate(
                            event.route
                        )
                    }
                }
            }
        }
    }

    val uiState = userRepository.userData.mapLatest { userData ->

        SignInUiState(
            loading = false,
            signedIn = userData.authenticatedWithEmail
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SignInUiState()
    )

    fun signInWithEmail(email: String, password: String) {

        userRepository.authenticateWithMail(email = email, password = password) { exception ->

            when (exception) {
                null -> setEffect {
                    SignInEffect.Navigate(
                        when (settings.settingsState.value.launchScreen) {
                            LaunchScreen.Crypto -> "crypto"
                            LaunchScreen.News -> "news"
                        }
                    )
                }
                else -> {
                    Log.d("Auth Test", "Couldn't authenticate: ${exception.message}")
                }
            }
        }
    }

    fun logout() {

        when (userRepository.signOut()) {
            true -> setEffect {
                SignInEffect.Navigate(
                    route = "loginScreen"
                )
            }
            false -> {
                Log.d("Auth Test", "Couldn't logout")
            }
        }

    }
}