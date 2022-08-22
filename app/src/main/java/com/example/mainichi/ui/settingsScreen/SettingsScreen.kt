package com.example.mainichi.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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

    Column(modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(24.dp)) {

        Text(
            text = "Create new Notification",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val selectedAssetMap = remember { mutableStateMapOf<String, Boolean>() }
        val selectedEventMap = remember { mutableStateMapOf<String, Boolean>() }
        val selectedIntervalMap = remember { mutableStateMapOf<String, Boolean>() }

        val content = listOf("Bitcoin", "Ethereum", "Cardano")

        content.forEach {
            selectedAssetMap[it] = false
            selectedEventMap[it] = false
            selectedIntervalMap[it] = false
        }


        SelectableDropDown(
            title = "Asset",
            content = content,
            selectedMap = selectedAssetMap,
            onSelect = { name ->

                remapSelection(selectedAssetMap, name)
            })

        SelectableDropDown(
            title = "Event",
            content = content,
            selectedMap = selectedEventMap,
            onSelect = { name ->
                remapSelection(selectedEventMap, name)
            }
        )
//
            SelectableDropDown(
                title = "Interval",
                content = content,
                selectedMap = selectedIntervalMap,
                onSelect = { name ->
                    remapSelection(selectedIntervalMap, name)
                }
            )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(100.dp, 60.dp)
                .padding(top = 16.dp)
        ) {
            Text(text = "Save")
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

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        var expanded by remember { mutableStateOf(false) }
        var selectedAsset by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .background(MaterialTheme.colors.onBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                color = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    })

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

                content.forEach { name ->

                    DropdownMenuItem(onClick = {

                        onSelect(name)
                        selectedAsset = name

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.onBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = selectedAsset,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}
