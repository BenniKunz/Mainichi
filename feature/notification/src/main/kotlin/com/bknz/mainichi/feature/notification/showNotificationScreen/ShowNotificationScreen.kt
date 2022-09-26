package com.bknz.mainichi.feature.notification.showNotificationScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bknz.mainichi.feature.notification.R
import com.bknz.mainichi.ui.LoadingStateProgressIndicator
import com.bknz.mainichi.feature.notification.showNotificationScreen.ShowNotificationContract.*
import com.bknz.mainichi.feature.notification.showNotificationScreen.ShowNotificationContract.ShowNotificationEvent.*


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
        onNavigateUp = onNavigateUp,
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNotificationScreen(
    state: UiState,
    onViewModelEvent: (ShowNotificationEvent) -> Unit,
    onNavigateUp: () -> Unit
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

                NotificationScreenContent(
                    state = state,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun NotificationScreenContent(
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

                            val dismissState = rememberDismissState(
                                confirmStateChange = { dismissValue ->
                                    when (dismissValue) {
                                        DismissValue.Default -> {
                                            false
                                        }
                                        DismissValue.DismissedToEnd -> {
                                            true
                                        }
                                        DismissValue.DismissedToStart -> {
                                            true
                                        }
                                    }
                                }
                            )
                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(
                                    DismissDirection.EndToStart,
                                    DismissDirection.StartToEnd
                                ),
                                background = {

                                    val direction =
                                        dismissState.dismissDirection ?: return@SwipeToDismiss

                                    val color by animateColorAsState(
                                        targetValue =
                                        when (dismissState.targetValue) {
                                            DismissValue.Default -> Color.Gray
                                            DismissValue.DismissedToEnd -> Color.Yellow
                                            DismissValue.DismissedToStart -> Color.Red
                                        }
                                    )

                                    val scale by animateFloatAsState(
                                        targetValue = if (dismissState.targetValue == DismissValue.Default) {
                                            0.7f
                                        } else {
                                            1.2f
                                        }
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = color)
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = when (direction) {
                                            DismissDirection.StartToEnd -> Arrangement.Start
                                            DismissDirection.EndToStart -> Arrangement.End
                                        }
                                    ) {
                                        Icon(
                                            when (direction) {
                                                DismissDirection.StartToEnd -> Icons.Default.Edit
                                                DismissDirection.EndToStart -> Icons.Default.Delete
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.scale(scale = scale)
                                        )

                                        Text(
                                            text = when (direction) {
                                                DismissDirection.StartToEnd -> "Edit"
                                                DismissDirection.EndToStart -> "Delete"
                                            }
                                        )
                                    }
                                },
                                dismissContent = {
                                    NotificationsCard(notificationConfiguration = notificationConfiguration)
                                })
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.End
        ) {

            ExtendedFloatingActionButton(

                text = {
                    Text(
                        text = "New Notification",
                        color = MaterialTheme.colors.secondaryVariant
                    )
                },

                onClick = {
                    onViewModelEvent(NavigateToCreateNotificationScreen)
                },

                backgroundColor = MaterialTheme.colors.primaryVariant,

                contentColor = Color.White,

                icon = { Icon(Icons.Filled.Add, "") }
            )
        }
    }
}