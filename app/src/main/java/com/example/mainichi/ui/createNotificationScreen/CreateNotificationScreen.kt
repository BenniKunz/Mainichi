package com.example.mainichi.ui.createNotificationScreen

import SectionHeader
import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mainichi.R
import com.example.mainichi.core.model.Asset
import com.example.mainichi.ui.ImageLoader
import com.example.mainichi.ui.LoadingStateProgressIndicator
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.*
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.NotificationConfiguration.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateNotificationScreen(
    viewModel: CreateNotificationViewModel,
    onDismissDialog: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onDismissDialog) {

        viewModel.effect.collect {
            when (it) {

                else -> {}
            }
        }
    }

    CreateNotificationScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onDismissDialog = onDismissDialog
    )

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNotificationScreen(
    state: UiState,
    onViewModelEvent: (CreateNotificationEvent) -> Unit,
    onDismissDialog: () -> Unit,
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
                        onDismissDialog()
                    }) {

                        Icon(
                            imageVector = Icons.Default.Close,
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
    onViewModelEvent: (CreateNotificationEvent) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        horizontalAlignment = Alignment.Start
    ) {

        item {

            SectionHeader(
                text = "Create new Notification",
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        item {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
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
                        LazyChipRow(
                            sublist = sublist,
                            onViewModelEvent = onViewModelEvent,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                        )
                    }
                }
            }

        item {
            SectionHeader(
                text = "Event",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
        }


        item {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {

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

                SectionHeader(
                    text = "Price Up",
                    modifier = Modifier.padding(end = 32.dp)
                )

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

                SectionHeader(
                    text = "Price Down"
                )
            }

        }

        item {
            var checkedState by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf("") }

            SectionHeader(
                text = "Event Threshold in %",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                    OutlinedTextField(
                        value = when {
                            checkedState -> ""
                            else -> text
                        },
                        label = {
                            Text(
                                text = "Percentage",
                                style = MaterialTheme.typography.caption
                            )
                        },
                        onValueChange = {
                            var newText = ""

                            when {
                                it.isNotEmpty() && it[it.length - 1] == '.' -> {
                                    newText = it.removeRange(it.length - 1, it.length)
                                }
                                it.length > 2 -> {
                                    newText = it.substring(0, 2)
                                }
                                else -> {
                                    newText = it
                                }
                            }

                            text = newText
                            onViewModelEvent(
                                CreateNotificationEvent.ChangeEventValue(
                                    eventValue = newText
                                )
                            )
                        },
                        enabled = when {
                            checkedState -> false
                            else -> true
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                    )
                }


                Checkbox(checked = checkedState, onCheckedChange = {
                    checkedState = !checkedState

                    onViewModelEvent(CreateNotificationEvent.ToggleAnyValue)
                })

                Text(
                    text = "Any  Change",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        item {

            SectionHeader(
                text = "Interval",
                modifier = Modifier.padding(start = 16.dp)
            )

            var expandedIntervalField by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf("") }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                    OutlinedTextField(
                        value = text,
                        label = {
                            Text(
                                text = "Value",
                                style = MaterialTheme.typography.caption
                            )
                        },
                        onValueChange = {
                            var newText = ""

                            when {
                                it.isNotEmpty() && it[it.length - 1] == '.' -> {
                                    newText = it.removeRange(it.length - 1, it.length)
                                }
                                it.length > 2 -> {
                                    newText = it.substring(0, 2)
                                }
                                else -> {
                                    newText = it
                                }
                            }

                            text = newText
                            onViewModelEvent(
                                CreateNotificationEvent.ChangeIntervalValue(
                                    intervalValue = newText
                                )
                            )
                        },
                        enabled = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        modifier = Modifier.width(120.dp)
                    )
                }


                Box {
                    OutlinedTextField(
                        value = state.notificationConfiguration.intervalType.toString(),
                        label = { Text(text = "Period") },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    ),
                    enabled = state.notificationConfiguration.assets[0].isSelected
                            && state.notificationConfiguration.eventType != PriceEvent.None
                            && state.notificationConfiguration.intervalValue.isNotEmpty()
                            && (state.notificationConfiguration.eventValue.isNotEmpty() || state.notificationConfiguration.anyEventValue),
                    onClick = {
                        onViewModelEvent(CreateNotificationEvent.CreateCustomNotification)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp, 60.dp)

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

@Composable
private fun LazyChipRow(
    sublist: List<Asset>,
    onViewModelEvent: (CreateNotificationEvent) -> Unit,
    modifier: Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        sublist.forEach { selectableAsset ->
            item {
                IconChip(
                    text = selectableAsset.name,
                    image = selectableAsset.image,
                    isSelected = selectableAsset.isSelected,
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
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }

}