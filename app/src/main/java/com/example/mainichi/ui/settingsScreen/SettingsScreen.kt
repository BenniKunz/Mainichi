package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.settingsScreen.SettingsContract.NotificationConfiguration.*


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

            SettingsContent(
                state = state,
                onViewModelEvent = onViewModelEvent
            )
        }
    }
}

@Composable
fun SettingsContent(
    state: SettingsContract.UiState,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        item {

            SectionHeader(text = "Create new Notification")
        }

        item {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader(text = "Asset")

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ExpandCircleDown,
                        contentDescription = null
                    )
                }
            }
        }

        state.notificationConfiguration.assets.chunked(3)
            .forEachIndexed() { index, sublist ->

                if (expanded || index == 0) {
                    item {
                        LazyChipRow(sublist, onViewModelEvent)
                    }
                }
            }

        item {

            SectionHeader(text = "Event")

            Row(verticalAlignment = Alignment.CenterVertically) {

                var checkedStateUp by remember { mutableStateOf(false) }
                var checkedStateDown by remember { mutableStateOf(false) }

                Checkbox(checked = checkedStateUp, onCheckedChange = {
                    checkedStateUp = !checkedStateUp

                    when {
                        !checkedStateUp && !checkedStateDown -> onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangePriceEvent(
                                PriceEvent.None
                            )
                        )
                        checkedStateUp && checkedStateDown -> onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangePriceEvent(
                                PriceEvent.PriceUpDown
                            )
                        )
                        checkedStateUp -> onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangePriceEvent(
                                PriceEvent.PriceUp
                            )
                        )
                    }
                })

                SectionHeader(text = "Price Up")

                Checkbox(checked = checkedStateDown, onCheckedChange = {
                    checkedStateDown = !checkedStateDown

                    when {
                        !checkedStateUp && !checkedStateDown -> onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangePriceEvent(
                                PriceEvent.None
                            )
                        )
                        checkedStateUp && checkedStateDown -> onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangePriceEvent(
                                PriceEvent.PriceUpDown
                            )
                        )
                        checkedStateDown -> onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangePriceEvent(
                                PriceEvent.PriceDown
                            )
                        )
                    }
                })

                SectionHeader(text = "Price Down")
            }

        }

        item {
            var checkedState by remember { mutableStateOf(false) }

            SectionHeader(text = "Event Threshold")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = when {
                        checkedState -> ""
                        else -> state.notificationConfiguration.eventValue
                    },
                    label = { Text(text = "event value") },
                    onValueChange = {
                        onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangeEventValue(
                                eventValue = it
                            )
                        )
                    },
                    enabled = when {
                        checkedState -> false
                        else -> true
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    modifier = Modifier.width(120.dp)
                )

                Checkbox(checked = checkedState, onCheckedChange = {
                    checkedState = !checkedState

                    onViewModelEvent(SettingsContract.SettingsEvent.ToggleAnyValue)
                })

                Text(text = "Any Value Change")
            }
        }

        item {

            SectionHeader(text = "Interval")

            var expandedIntervalField by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf("") }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = state.notificationConfiguration.notificationInterval,
                    label = { Text(text = "interval value") },
                    onValueChange = {

                        onViewModelEvent(
                            SettingsContract.SettingsEvent.ChangeIntervalValue(
                                intervalValue = it
                            )
                        )
                    },
                    enabled = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    modifier = Modifier.width(120.dp)
                )

                Box {
                    OutlinedTextField(
                        value = state.notificationConfiguration.intervalPeriod.toString(),
                        label = { Text(text = "interval period") },
                        onValueChange = {},
                        trailingIcon = {

                            IconButton(onClick = {
                                expandedIntervalField = !expandedIntervalField
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        },
                        enabled = false,
                        modifier = Modifier.width(140.dp)
                    )

                    DropdownMenu(
                        expanded = expandedIntervalField,
                        onDismissRequest = { expandedIntervalField = false },
                        modifier = Modifier.heightIn(max = 220.dp)
                    ) {

                        enumValues<Periodically>().forEach { priceEvent ->

                            DropdownMenuItem(onClick = {

                                onViewModelEvent(
                                    SettingsContract.SettingsEvent.SelectIntervalPeriod(
                                        period = priceEvent
                                    )
                                )

                            }) {

                                Row(horizontalArrangement = Arrangement.Center) {
                                    Text(text = priceEvent.toString())
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ),
                enabled = state.notificationConfiguration.assets[0].selected
                        && state.notificationConfiguration.priceEvent != PriceEvent.None
                        && state.notificationConfiguration.notificationInterval.isNotEmpty()
                        && (state.notificationConfiguration.eventValue.isNotEmpty() || state.notificationConfiguration.anyEventValue),
                onClick = {
                    onViewModelEvent(SettingsContract.SettingsEvent.CreateCustomNotification)
                },
                modifier = Modifier
                    .size(100.dp, 60.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Save",
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}

@Composable
private fun LazyChipRow(
    sublist: List<SelectableAsset>,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        sublist.forEach { selectableAsset ->
            item {
                IconChip(
                    text = selectableAsset.name,
                    image = selectableAsset.image,
                    isSelected = selectableAsset.selected,
                    onSelected = {
                        onViewModelEvent(
                            SettingsContract.SettingsEvent.SelectAsset(
                                selectedAsset = selectableAsset
                            )
                        )
                    },
                    color = MaterialTheme.colors.background
                )
            }
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(end = 8.dp)
    )
}

@Composable
fun IconChip(
    text: String,
    image: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    color: Color
) {

    Surface(
        elevation = 8.dp,
        color = if (isSelected) {
            MaterialTheme.colors.primary
        } else {
            color
        },
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .border(
                1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(32.dp)
            )
    ) {

        Row(
            modifier = Modifier
                .clickable {
                    onSelected()
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ImageLoader(
                data = image, modifier = Modifier
                    .size(20.dp)
            )

            Text(
                text = text,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )

        }
    }

}