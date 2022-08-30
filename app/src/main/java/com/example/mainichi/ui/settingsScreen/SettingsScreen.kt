package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mainichi.R
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigate: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel) {

        viewModel.effect.collect {
            when (it) {

                is SettingsContract.SettingsEffect.NavigateToShowNotificationsScreen -> {
                    onNavigate()
                }
            }
        }
    }


    SettingsScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onNavigateUp = onNavigateUp
    )
}


@Composable
fun SettingsScreen(
    state: UiState,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit,
    onNavigateUp: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colors.primary
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary,
                navigationIcon = {

                    IconButton(onClick = {
                        onNavigateUp()
                    }) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Open menu",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        },

        ) {
        when {
            state.isLoading -> LoadingStateProgressIndicator(
                color = MaterialTheme.colors.onBackground,
                size = 50
            )
            else -> {

                ShowSettingsScreen(
                    state = state,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@Composable
fun ShowSettingsScreen(
    state: UiState,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(state.settings) { setting ->
            SettingsRow(
                setting = setting,
                onViewModelEvent = onViewModelEvent
            )
        }
    }
}

@Composable
fun SettingsRow(
    setting: Setting,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onViewModelEvent(setting.target) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(setting.title)

        Icon(Icons.Default.ExpandMore, contentDescription = null)

    }
}