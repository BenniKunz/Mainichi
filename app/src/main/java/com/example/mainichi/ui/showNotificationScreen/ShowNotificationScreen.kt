package com.example.mainichi.ui.createNotificationScreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mainichi.R
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract.*
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract.ShowNotificationEvent.*
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNotificationScreen(
    viewModel: ShowNotificationViewModel,
    onNavigateUp: () -> Unit,
    onNavigate: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigateUp) {

        viewModel.effect.collect {
            when (it) {
                ShowNotificationEffect.NavigateToCreateNotificationScreen -> {
                    onNavigate()
                }
            }
        }
    }

    ShowNotificationScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onNavigateUp = onNavigateUp
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNotificationScreen(
    state: UiState,
    onViewModelEvent: (ShowNotificationEvent) -> Unit,
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

                ShowNotificationScreen(
                    state = state,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNotificationScreen(
    state: UiState,
    onViewModelEvent: (ShowNotificationEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row() {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {


                if (state.notificationConfigurations.isEmpty()) {

                    item {
                        Row() {

                            Text(
                                text = "No notifications",
                                modifier = Modifier.padding(end = 16.dp)
                            )

                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                            )
                        }

                    }
                } else {
                    state.notificationConfigurations.forEach { notificationConfiguration ->
                        item {
                            NotificationsCard(notificationConfiguration = notificationConfiguration)
                        }
                    }
                }
            }
        }

        var showCreationDialog by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Button(onClick = {

                showCreationDialog = !showCreationDialog


            }) {
                Text("New Notification")
            }
        }

        if (showCreationDialog) {

            CreateNotificationScreen(
                viewModel = hiltViewModel(),
                onDismissDialog = { showCreationDialog = false },
            )
        }
    }
}