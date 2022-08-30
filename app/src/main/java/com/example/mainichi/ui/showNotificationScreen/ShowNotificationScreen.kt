package com.example.mainichi.ui.createNotificationScreen

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mainichi.R
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNotificationScreen(
    viewModel: ShowNotificationViewModel,
    onNavigateUp: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigateUp) {

        viewModel.effect.collect {
            when (it) {

                else -> {}
            }
        }
    }

    ShowNotificationScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onNavigateUp = onNavigateUp
    )

}

@Composable
fun ShowNotificationScreen(
    state: ShowNotificationContract.UiState,
    onViewModelEvent: (ShowNotificationContract.ShowNotificationEvent) -> Unit,
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

@Composable
fun ShowNotificationScreen(
    state: ShowNotificationContract.UiState,
    onViewModelEvent: (ShowNotificationContract.ShowNotificationEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        state.notificationConfigurations.forEach { notificationConfiguration ->
            item {
                NotificationsCard(notificationConfiguration = notificationConfiguration)
            }
        }
    }
}