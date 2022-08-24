package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUpRequested: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigateUpRequested) {

        viewModel.effect.collect {
            when (it) {

                else -> {}
            }
        }
    }

    SettingsScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) })

}

@Composable
fun SettingsScreen(
    state: SettingsContract.UiState,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {

    when {
        state.isLoading -> LoadingStateProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            size = 50
        )
        else -> {

            val selectedAssetMap = remember { mutableStateMapOf<String, Boolean>() }
            val selectedEventMap = remember { mutableStateMapOf<String, Boolean>() }
            val selectedEventValueMap = remember { mutableStateMapOf<String, Boolean>() }
            val selectedIntervalUnitMap = remember { mutableStateMapOf<String, Boolean>() }
            val selectedIntervalValueMap = remember { mutableStateMapOf<String, Boolean>() }

            state.assets.forEach {
                selectedAssetMap[it.name] = false
            }
            state.notificationParameters.priceEvents.forEach {
                selectedEventMap[it] = false
            }

            state.notificationParameters.percentageThresholds.forEach {
                selectedEventValueMap[it] = false
            }

            state.notificationParameters.intervalTypes.forEach {
                selectedIntervalUnitMap[it] = false
            }

            state.notificationParameters.intervalValues.forEach {
                selectedIntervalValueMap[it] = false
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Text(
                        text = "Create new Notification",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    SelectableDropDown(
                        title = "Asset",
                        content = state.assets.map { it.name },
                        selectedMap = selectedAssetMap,
                        onSelect = { name ->

                            remapSelection(selectedAssetMap, name)
                        })
                }

                item {
                    SelectableDropDown(
                        title = "Event",
                        content = state.notificationParameters.priceEvents,
                        selectedMap = selectedEventMap,
                        onSelect = { name ->
                            remapSelection(selectedEventMap, name)
                        }
                    )
                }

                item {
                    SelectableDropDown(
                        title = "Event Value",
                        content = state.notificationParameters.percentageThresholds,
                        selectedMap = selectedEventValueMap,
                        onSelect = { name ->
                            remapSelection(selectedEventValueMap, name)
                        }
                    )
                }


                item {
                    SelectableDropDown(
                        title = "Interval Unit",
                        content = state.notificationParameters.intervalTypes,
                        selectedMap = selectedIntervalUnitMap,
                        onSelect = { name ->
                            remapSelection(selectedIntervalUnitMap, name)
                        }
                    )
                }

                item {
                    SelectableDropDown(
                        title = "Interval value",
                        content = state.notificationParameters.intervalValues,
                        selectedMap = selectedIntervalValueMap,
                        onSelect = { name ->
                            remapSelection(selectedIntervalValueMap, name)
                        }
                    )
                }


                item {
                    Button(
                        onClick = {
                            onViewModelEvent(
                                SettingsContract.SettingsEvent.CreateCustomNotification(
                                    asset = selectedAssetMap.filter { it.value }.keys.first(),
                                    eventType = selectedEventMap.filter { it.value }.keys.first(),
                                    eventValue = selectedEventValueMap.filter { it.value }.keys.first(),
                                    repeatIntervalTimeUnit = if (selectedIntervalValueMap.filter { it.value }.keys.first() == "Day") {
                                        TimeUnit.DAYS
                                    } else {
                                        TimeUnit.HOURS
                                    },
                                    repeatInterval = selectedIntervalValueMap.filter { it.value }.keys.first()
                                        .toLong()

                                )
                            )
                        },
                        modifier = Modifier
                            .size(100.dp, 60.dp)
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Save")
                    }
                }

                item {
                    Text(text = "Created Notifications")
                }

                state.notifications.forEach {
                    item {
                        NotificationsCard(
                            it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsCard(
    notification: AssetNotification
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageLoader(
            data = notification.image,
            modifier = Modifier.size(32.dp)
        )

        Text(notification.symbol.uppercase())

        Text(notification.event)

        Text(notification.interval)

        Text(notification.date)
    }
}


private fun remapSelection(
    selectedAssetMap: SnapshotStateMap<String, Boolean>,
    name: String
) {
    selectedAssetMap.forEach { (s, _) ->

        if (s == name) {
            selectedAssetMap[name] = true
        } else {
            selectedAssetMap[s] = false
        }
    }
}

@Composable
fun SelectableDropDown(
    title: String,
    content: List<String>,
    selectedMap: SnapshotStateMap<String, Boolean>,
    onSelect: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Button(
        onClick = { expanded = !expanded },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onBackground,
        ),
        modifier = Modifier.size(200.dp, 40.dp)
    ) {

        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "$title: ",
                color = MaterialTheme.colors.primary
            )

            Text(
                text = selectedOption,
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 220.dp)
        ) {

            content.forEach { name ->

                DropdownMenuItem(onClick = {

                    onSelect(name)
                    selectedOption = name

                }) {

                    Row() {
                        Icon(
                            imageVector = if (selectedMap[name] == true) {
                                Icons.Filled.CheckCircle
                            } else {
                                Icons.Default.Check
                            }, contentDescription = null
                        )

                        Text(text = name)
                    }
                }
            }
        }
    }
}