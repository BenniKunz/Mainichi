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
import com.example.mainichi.ui.settingsScreen.SettingsContract.NotificationConfiguration
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
        state.notificationConfiguration != null -> {

            var expanded by remember { mutableStateOf(false) }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {

                item {

                    Text(
                        text = "Create new Notification",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                item {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Asset")

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

                        if (expanded) {
                            item {
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
                        } else if (index == 0) {
                            item {
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
                        }
                    }


                item {

                    Text(text = "Event")

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

                        Text(text = "Price Up")


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

                        Text(text = "Price Down")
                    }

                }

                item {
                    var checkedState by remember { mutableStateOf(false) }

                    Text(text = "Event Threshold")

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedTextField(
                            value = when {
                                checkedState -> ""
                                else -> state.notificationConfiguration.eventValue
                            },
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
                            modifier = Modifier.width(80.dp)
                        )

                        Checkbox(checked = checkedState, onCheckedChange = {
                            checkedState = !checkedState

                            onViewModelEvent(SettingsContract.SettingsEvent.ToggleAnyValue)
                        })

                        Text(text = "Any Value Change")
                    }
                }

                item {

                    Text(text = "Interval")

                    var expandedIntervalField by remember { mutableStateOf(false) }
                    var text by remember { mutableStateOf("") }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(modifier = Modifier.fillMaxWidth(0.2f)) {
                            OutlinedTextField(
                                value = state.notificationConfiguration.notificationInterval,
                                onValueChange = {

                                    onViewModelEvent(
                                        SettingsContract.SettingsEvent.ChangeIntervalValue(
                                            intervalValue = it
                                        )
                                    )
                                },
                                enabled = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                maxLines = 1
                            )
                        }

                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                            OutlinedTextField(
                                value = state.notificationConfiguration.intervalPeriod.toString(),
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
                                enabled = false
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
    }
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

fun MutableMap<String, Boolean>.setFalse() {
    this.entries.forEach {
        if (it.value) {
            it.setValue(false)
        }
    }
}

@Composable
fun NotificationsCard(
    notification: AssetNotification
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageLoader(
            data = notification.image,
            modifier = Modifier.size(32.dp)
        )

        Column() {

            Text(notification.symbol.uppercase())

            Text(
                text = notification.date,
                style = MaterialTheme.typography.caption
            )
        }

        Column() {
            Text(notification.event)

            Text(
                text = "every ${notification.interval} ${notification.intervalType}",
                style = MaterialTheme.typography.caption
            )
        }
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text(title) },
            enabled = false,
            modifier = Modifier.clickable {
                expanded = !expanded
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 220.dp)
        ) {

            content.forEach { asset ->

                DropdownMenuItem(onClick = {

                    onSelect(asset)
                    selectedOption = asset

                }) {

                    Row() {
                        Text(text = asset)
                    }
                }
            }
        }
    }
}

//@Composable
//fun SelectableDropDown(
//    title: String,
//    content: List<CategoryItem>,
//    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
//) {
//
//    var expanded by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally) {
//        TextField(
//            value = content.find { it.selected }?.categoryType?.categoryName ?: "",
//            onValueChange = { },
//            label = { Text(title) },
//            enabled = false,
//            modifier = Modifier.clickable {
//                expanded = !expanded
//            }
//        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.heightIn(max = 220.dp)
//        ) {
//
//            content.forEach { item ->
//
//                DropdownMenuItem(onClick = {
//
//                    onViewModelEvent(
//                        SettingsContract.SettingsEvent.SelectCategoryItem(
//                            categoryType = item.categoryType
//                        )
//                    )
//
//                }) {
//
//                    Row(horizontalArrangement = Arrangement.Center) {
//                        Text(text = item.categoryType.categoryName)
//                    }
//                }
//            }
//        }
//    }
//}