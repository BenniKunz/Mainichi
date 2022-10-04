package com.bknz.mainichi.feature.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bknz.mainichi.core.designsystem.MainichiTheme
import com.bknz.mainichi.ui.LoadingStateProgressIndicator
import com.bknz.mainichi.ui.UserInteractionButton

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigate: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel) {

        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.Navigate -> {
                    onNavigate(effect.route)
                }
            }
        }
    }

    when {

        uiState.loading -> {
            LoadingStateProgressIndicator()
        }
        else -> LoginScreen(
            state = uiState,
            onAnonymousLogin = { viewModel.loginAnonymously() },
            navigate = { event -> viewModel.setEvent(event) })
    }
}

@Composable
internal fun LoginScreen(
    state: LoginUiState,
    onAnonymousLogin: () -> Unit,
    navigate: (LoginEvent) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterVertically)
    ) {

        UserInteractionButton(
            text = "Sign up",
            modifier = Modifier
                .background(MaterialTheme.colors.onBackground)
        ) {
            navigate(LoginEvent.Navigate("signUp"))
        }

        UserInteractionButton(
            text = "Sign in",
            modifier = Modifier.background(MaterialTheme.colors.onPrimary)
        ) {
            navigate(LoginEvent.Navigate("signIn"))
        }

        UserInteractionButton(
            text = "Explore without Login",
            modifier = Modifier.background(MaterialTheme.colors.onPrimary)
        ) {
            onAnonymousLogin()
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppBar() {
    MainichiTheme() {
        LoginScreen(state = LoginUiState(loading = false), {}, {})
    }
}