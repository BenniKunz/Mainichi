package com.bknz.mainichi.feature.signUp

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bknz.mainichi.core.designsystem.MainichiTheme
import com.bknz.mainichi.feature.login.R
import com.bknz.mainichi.ui.LoadingStateProgressIndicator
import com.bknz.mainichi.ui.UserInteractionButton
import com.bknz.mainichi.ui.UserInteractionField

@Composable
internal fun SignUpScreen(
    viewModel: SignUpViewModel,
    onNavigate: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel) {

        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpEffect.Navigate -> {
                    onNavigate(effect.route)
                }
            }
        }
    }

    when {

        uiState.loading -> {
            LoadingStateProgressIndicator()
        }
        else -> SignUpScreen(
            state = uiState,
            onSignUp = { email: String, password: String ->
                viewModel.signUpWithEmail(
                    email = email,
                    password = password
                )
            })
    }
}

@Composable
internal fun SignUpScreen(
    state: UiState,
    onSignUp: (String, String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        UserInteractionField(
            value = email,
            onNewValue = { email = it },
            placeholder = stringResource(id = R.string.email)
        )

        UserInteractionField(
            value = password,
            onNewValue = { password = it },
            placeholder = stringResource(id = R.string.password)
        )

        UserInteractionButton(
            text = "Sign up",
            modifier = Modifier.background(color = MaterialTheme.colors.onBackground)) {
            onSignUp(email, password)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppBar() {
    MainichiTheme() {
        SignUpScreen(state = UiState(loading = false)) { _: String, _: String -> }
    }
}