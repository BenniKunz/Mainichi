package com.example.mainichi.ui.createNotificationScreen

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mainichi.R
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.*
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.NotificationConfiguration.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateNotificationScreen(
    viewModel: CreateNotificationViewModel,
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

    CreateNotificationScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onNavigateUp = onNavigateUp
    )

}

@Composable
fun CreateNotificationScreen(
    state: UiState,
    onViewModelEvent: (CreateNotificationEvent) -> Unit,
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

                SettingsContent(
                    state = state,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@Composable
fun SettingsContent(
    state: UiState,
    onViewModelEvent: (CreateNotificationEvent) -> Unit
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
                            CreateNotificationEvent.ChangePriceEvent(
                                PriceEvent.None
                            )
                        )
                        checkedStateUp && checkedStateDown -> onViewModelEvent(
                            CreateNotificationEvent.ChangePriceEvent(
                                PriceEvent.PriceUpDown
                            )
                        )
                        checkedStateUp -> onViewModelEvent(
                            CreateNotificationEvent.ChangePriceEvent(
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
                            CreateNotificationEvent.ChangePriceEvent(
                                PriceEvent.None
                            )
                        )
                        checkedStateUp && checkedStateDown -> onViewModelEvent(
                            CreateNotificationEvent.ChangePriceEvent(
                                PriceEvent.PriceUpDown
                            )
                        )
                        checkedStateDown -> onViewModelEvent(
                            CreateNotificationEvent.ChangePriceEvent(
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
                            CreateNotificationEvent.ChangeEventValue(
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

                    onViewModelEvent(CreateNotificationEvent.ToggleAnyValue)
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
                    value = state.notificationConfiguration.intervalValue,
                    label = { Text(text = "interval value") },
                    onValueChange = {

                        onViewModelEvent(
                            CreateNotificationEvent.ChangeIntervalValue(
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
                        value = state.notificationConfiguration.intervalType.toString(),
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
                                    CreateNotificationEvent.SelectIntervalPeriod(
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
                        && state.notificationConfiguration.eventType != PriceEvent.None
                        && state.notificationConfiguration.intervalValue.isNotEmpty()
                        && (state.notificationConfiguration.eventValue.isNotEmpty() || state.notificationConfiguration.anyEventValue),
                onClick = {
                    onViewModelEvent(CreateNotificationEvent.CreateCustomNotification)
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
    sublist: List<NotificationAsset>,
    onViewModelEvent: (CreateNotificationEvent) -> Unit
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
                            CreateNotificationEvent.SelectAsset(
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