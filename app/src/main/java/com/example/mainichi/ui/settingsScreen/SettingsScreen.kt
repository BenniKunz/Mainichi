package com.example.mainichi.ui.settingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier


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

    SettingsScreen()

}

@Composable
fun SettingsScreen(

) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Create new Notification")

        var selectedAssetMap = remember { mutableStateMapOf<String, Boolean>() }
        var selectedEventMap = remember { mutableStateMapOf<String, Boolean>() }
        var selectedIntervalMap = remember { mutableStateMapOf<String, Boolean>() }

        val content = listOf("Bitcoin", "Ethereum", "Cardano")

        content.forEach {
            selectedAssetMap[it] = false
            selectedEventMap[it] = false
            selectedIntervalMap[it] = false
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            SelectableDropDown("Asset", content, selectedAssetMap)

            SelectableDropDown("Event", content, selectedEventMap)

            SelectableDropDown("Interval", content, selectedIntervalMap)
        }
    }
}

@Composable
fun SelectableDropDown(
    title: String,
    content: List<String>,
    selectedMap: SnapshotStateMap<String, Boolean>
) {

    Column() {

        var expanded by remember { mutableStateOf(false) }
        var asset by remember { mutableStateOf("") }

        Text(text = title, modifier = Modifier.clickable {
            expanded = !expanded
        })

        Text(text = asset)

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

            content.forEach { name ->

                DropdownMenuItem(onClick = {

                    selectedMap.forEach { (s, _) ->

                        if (s == name) {
                            selectedMap[name] = true
                        } else {
                            selectedMap[s] = false
                        }
                    }

                    asset = name
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
