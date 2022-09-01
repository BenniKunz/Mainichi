package com.example.mainichi.ui.settingsScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mainichi.R
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.createNotificationScreen.SectionHeader
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState
import com.example.mainichi.ui.settingsScreen.SettingsContract.UiState.*
import com.example.mainichi.ui.settingsScreen.launchScreenDialog.LaunchScreenDialog


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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSettingsScreen(
    state: UiState,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(16.dp)
    ) {

        item {
            SectionHeader(text = "Settings", style = MaterialTheme.typography.h4)
        }

        enumValues<Setting>().forEach { setting ->

            item {
                SettingsRow(
                    title = setting.asString(),
                    imageVector = setting.getImageVector(state.isDarkMode),
                    onViewModelEvent = onViewModelEvent,
                    event = setting.getEvent()
                )

                Divider()
            }
        }
    }

    if(state.setLaunchScreen) {
        LaunchScreenDialog(
            viewModel = hiltViewModel(),
            onDismissDialog = { onViewModelEvent(SettingsContract.SettingsEvent.ChangeLaunchScreen)}
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    imageVector: ImageVector,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit,
    event: SettingsContract.SettingsEvent
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onViewModelEvent(event) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(title)

        Icon(imageVector, contentDescription = null)

    }
}