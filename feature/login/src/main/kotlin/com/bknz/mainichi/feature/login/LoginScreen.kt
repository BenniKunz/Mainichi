package com.bknz.mainichi.feature.login

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bknz.mainichi.ui.LoadingStateProgressIndicator

@androidx.compose.runtime.Composable
internal fun LoginScreen(viewModel: LoginViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    when {

        uiState.loading -> {
            LoadingStateProgressIndicator()
        }
        else -> LoginScreen(
            state = uiState,
            onAuth = { viewModel.authenticate() })

    }

}

@Composable
internal fun LoginScreen(
    state: UiState,
    onAuth: () -> Unit
) {

    Button(onClick = {
        onAuth()
    }
    ) {

        Text("Authenticate with Mail")
    }

}