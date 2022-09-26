package com.bknz.mainichi.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bknz.mainichi.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

internal data class UiState(
    val loading: Boolean = true,
    val authenticatedAnonymously: Boolean = false,
    val authenticatedCredentials: Boolean = false

)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState = userRepository.userData.mapLatest { userData ->

        UiState(
            loading = false,
            authenticatedAnonymously = userData.authenticatedAnonymously,
            authenticatedCredentials = userData.authenticatedCredentials
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UiState()
    )

    fun authenticate() {
        userRepository.createAnonymousAccount()
    }

}