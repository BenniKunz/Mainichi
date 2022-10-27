package com.bknz.mainichi.feature.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bknz.mainichi.core.designsystem.MainichiTheme
import com.bknz.mainichi.ui.LoadingStateProgressIndicator

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigate: (String) -> Unit,
    paddingValues: PaddingValues
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
            navigate = { event -> viewModel.setEvent(event) },
            paddingValues = paddingValues
        )
    }
}

@Composable
internal fun LoginScreen(
    state: LoginUiState,
    onAnonymousLogin: () -> Unit,
    navigate: (LoginEvent) -> Unit,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(64.dp, Alignment.Bottom)
    ) {

        Button(
            onClick = { onAnonymousLogin() },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .size(160.dp, 80.dp)
        ) {
            Text(text = "Explore without Login")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(
                onClick = { navigate(LoginEvent.Navigate("signUp")) },
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .size(160.dp, 80.dp)
            ) {
                Text(text = "SIGN UP")
            }

            Button(
                onClick = { navigate(LoginEvent.Navigate("signIn")) },
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .size(160.dp, 80.dp)
            ) {
                Text(text = "SIGN IN")
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppBar() {
    MainichiTheme() {
        LoginScreen(
            state = LoginUiState(loading = false),
            onAnonymousLogin = {},
            navigate = {},
            paddingValues = PaddingValues()
        )
    }
}