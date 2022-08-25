package com.example.mainichi.ui.settingsScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator


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

            state.assets.forEach { asset ->
                selectedAssetMap[asset.name] = false
            }

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
                        style = MaterialTheme.typography.h5
                    )
                }

                item {
                    SelectableDropDown(
                        title = "Asset",
                        content = state.assets.map { asset ->
                            asset.name
                        },
                        selectedMap = selectedAssetMap,
                        onSelect = { name ->
                            remapSelection(selectedAssetMap, name)
                        }
                    )
                }

                state.categoryMap.forEach { categoryList ->
                    item {
                        SelectableDropDown(
                            title = categoryList.key,
                            content = categoryList.value,
                            onViewModelEvent = onViewModelEvent
                        )
                    }
                }

                item {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        ),
                        enabled = true,
                        onClick = {
                            onViewModelEvent(
                                SettingsContract.SettingsEvent.CreateCustomNotification(
                                    asset = selectedAssetMap.filter { it.value }.keys.first()
                                )
                            )
                            selectedAssetMap.setFalse()

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

                if (state.notifications.isNotEmpty()) {
                    item {
                        Text(
                            text = "Created Notifications",
                            style = MaterialTheme.typography.h5,
                        )
                    }
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
    Column() {
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
                        Icon(
                            imageVector = Icons.Default.RadioButtonUnchecked,
                            contentDescription = null
                        )

                        Text(text = asset)
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableDropDown(
    title: String,
    content: List<CategoryItem>,
    onViewModelEvent: (SettingsContract.SettingsEvent) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column() {
        TextField(
            value = content.find { it.selected }?.categoryType?.categoryName ?: "",
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

            content.forEach { item ->

                DropdownMenuItem(onClick = {

                    onViewModelEvent(
                        SettingsContract.SettingsEvent.SelectCategoryItem(
                            categoryType = item.categoryType
                        )
                    )

                }) {

                    Row() {
                        Icon(
                            imageVector = Icons.Default.RadioButtonUnchecked,
                            contentDescription = null
                        )

                        Text(text = item.categoryType.categoryName)
                    }
                }
            }
        }
    }
}