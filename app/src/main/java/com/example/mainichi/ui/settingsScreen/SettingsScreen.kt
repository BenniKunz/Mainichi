package com.example.mainichi.ui.settingsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.settingsScreen.SettingsContract.NotificationConfiguration


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

                    Text(text = "Interval")

                    var expandedIntervalField by remember { mutableStateOf(false) }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {


                        Column(modifier = Modifier.fillMaxWidth(0.3f)) {
                            OutlinedTextField(
                                value = state.notificationConfiguration.notificationInterval.toString(),
                                onValueChange = {},
                                enabled = true
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

                                enumValues<NotificationConfiguration.Periodically>().forEach { priceEvent ->

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
                        enabled = true,
                        onClick = {

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