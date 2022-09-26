package com.bknz.mainichi.feature.settings.settingsScreen

import SectionHeader
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bknz.mainichi.feature.settings.settingsScreen.SettingsContract.UiState
import com.bknz.mainichi.feature.settings.settingsScreen.SettingsContract.UiState.*
import com.bknz.mainichi.feature.settings.settingsScreen.launchScreenDialog.LaunchScreenDialog
import com.bknz.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogScreen
import com.bknz.mainichi.ui.LoadingStateProgressIndicator

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigate: () -> Unit,
    onBackClick: () -> Unit
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
        onNavigateUp = onBackClick
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                        text = "",
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
            state.isLoading -> {
                LoadingStateProgressIndicator(
                    color = MaterialTheme.colors.onBackground,
                    size = 50
                )
            }

            else -> {

                ShowSettingsScreen(
                    state = state,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSettingsScreen(
    state: UiState,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(16.dp)
    ) {

        item {
            SectionHeader(text = "Settings", style = MaterialTheme.typography.h4)
        }

        enumValues<Setting>().forEach { setting ->

            item {
                SettingsRow(
                    title = setting.toString().asText(),
                    currentSetting = when (setting) {
                        Setting.Notifications -> "Create, delete, edit"
                        Setting.LaunchScreen -> state.currentLaunchScreen.toString().asText()
                        Setting.Theme -> state.currentTheme.toString().asText()
                    },
                    imageVector = setting.getImageVector(state.currentTheme),
                    onViewModelEvent = onViewModelEvent,
                    event = setting.getEvent()
                )
            }
        }
    }

    if (state.setTheme) {
        ThemeDialogScreen(
            viewModel = hiltViewModel(),
            onDismissDialog = { onViewModelEvent(SettingsContract.SettingsEvent.SetTheme) }
        )
    }

    if (state.setLaunchScreen) {
        LaunchScreenDialog(
            viewModel = hiltViewModel(),
            onDismissDialog = { onViewModelEvent(SettingsContract.SettingsEvent.ChangeLaunchScreen) }
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    currentSetting: String,
    imageVector: ImageVector,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit,
    event: SettingsContract.SettingsEvent
) {

    TextButton(onClick = { onViewModelEvent(event) }, modifier = Modifier.fillMaxWidth()) {

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector, contentDescription = null)

            Spacer(modifier = Modifier.size(24.dp))

            Column() {
                Text(text = title, color = MaterialTheme.colors.onBackground)

                Text(
                    text = currentSetting,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}