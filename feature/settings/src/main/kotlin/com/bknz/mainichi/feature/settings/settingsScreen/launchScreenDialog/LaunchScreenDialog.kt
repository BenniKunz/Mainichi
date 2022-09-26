package com.bknz.mainichi.feature.settings.settingsScreen.launchScreenDialog

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bknz.mainichi.feature.settings.settingsScreen.SettingsContract.UiState.*
import com.bknz.mainichi.ui.settingsScreen.launchScreenDialog.LaunchScreenDialogContract
import com.bknz.mainichi.ui.settingsScreen.launchScreenDialog.LaunchScreenDialogViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LaunchScreenDialog(
    viewModel: LaunchScreenDialogViewModel,
    onDismissDialog: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel) {

        viewModel.effect.collect {
            when (it) {

                else -> {}
            }
        }
    }


    LaunchScreenDialog(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onDismissDialog = onDismissDialog
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LaunchScreenDialog(
    state: LaunchScreenDialogContract.UiState,
    onViewModelEvent: (LaunchScreenDialogContract.LaunchScreenDialogEvent) -> Unit,
    onDismissDialog: () -> Unit,
) {
    when {
        state.isLoading -> {}
//            LoadingStateProgressIndicator(
//            color = MaterialTheme.colors.onBackground,
//            size = 50
//        )
        else -> {

            LaunchScreenContent(
                state = state,
                onViewModelEvent = onViewModelEvent,
                onDismissDialog = onDismissDialog
            )
        }
    }
}

@Composable
fun LaunchScreenContent(
    state: LaunchScreenDialogContract.UiState,
    onViewModelEvent: (LaunchScreenDialogContract.LaunchScreenDialogEvent) -> Unit,
    onDismissDialog: () -> Unit,
) {

    Dialog(onDismissRequest = { onDismissDialog() }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 0.dp, bottom = 16.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(color = MaterialTheme.colors.background.copy(alpha = 0.9f))
                .fillMaxWidth()
        ) {

            enumValues<LaunchScreen>().forEach { launchScreen ->

                item {
                    SelectableButton(
                        onClick = {
                            onViewModelEvent(
                                LaunchScreenDialogContract.LaunchScreenDialogEvent.SetLaunchScreen(
                                    launchScreen
                                )
                            )
                        },
                        imageVector = when {
                            launchScreen == LaunchScreen.Crypto && launchScreen == state.currentScreen -> Icons.Default.RadioButtonChecked
                            launchScreen == LaunchScreen.News && launchScreen == state.currentScreen -> Icons.Default.RadioButtonChecked
                            else -> Icons.Default.RadioButtonUnchecked
                        },
                        text = launchScreen.toString(),
                        tint = when {
                            launchScreen == LaunchScreen.Crypto && launchScreen == state.currentScreen -> MaterialTheme.colors.primary
                            launchScreen == LaunchScreen.News && launchScreen == state.currentScreen -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.onPrimary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectableButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String,
    tint: Color = MaterialTheme.colors.onPrimary,
    textColor: Color = MaterialTheme.colors.onPrimary
) {

    IconButton(onClick = { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
            ) {
                Text(text = text, color = textColor)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                )
            }
        }
    }
}