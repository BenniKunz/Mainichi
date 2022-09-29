package com.bknz.mainichi.feature.settings.settingsScreen.themeDialog

import com.bknz.mainichi.feature.settings.settingsScreen.launchScreenDialog.SelectableButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bknz.mainichi.core.model.Theme
import com.bknz.mainichi.feature.settings.settingsScreen.asText
import com.bknz.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogContract.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ThemeDialogScreen(
    viewModel: ThemeDialogViewModel,
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

    ThemeDialogScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) },
        onDismissDialog = onDismissDialog
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ThemeDialogScreen(
    state: UiState,
    onViewModelEvent: (ThemeDialogEvent) -> Unit,
    onDismissDialog: () -> Unit,
) {
    when {
        state.isLoading -> {
//            LoadingStateProgressIndicator(
//                color = MaterialTheme.colors.onBackground,
//                size = 50
//            )
        }
        else -> {

            ThemeDialogContent(
                state = state,
                onViewModelEvent = onViewModelEvent,
                onDismissDialog = onDismissDialog
            )
        }
    }
}

@Composable
fun ThemeDialogContent(
    state: UiState,
    onViewModelEvent: (ThemeDialogEvent) -> Unit,
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

            enumValues<Theme>().forEach { theme ->

                item {
                    SelectableButton(
                        onClick = {
                            onViewModelEvent(
                                ThemeDialogEvent.SetTheme(
                                    theme
                                )
                            )
                        },
                        imageVector = when {
                            theme == Theme.DarkMode && theme == state.currentTheme -> Icons.Default.RadioButtonChecked
                            theme == Theme.LightMode && theme == state.currentTheme -> Icons.Default.RadioButtonChecked
                            theme == Theme.SystemSetting && theme == state.currentTheme -> Icons.Default.RadioButtonChecked
                            else -> Icons.Default.RadioButtonUnchecked
                        },
                        text = theme.toString().asText(),
                        tint = when {
                            theme == Theme.DarkMode && theme == state.currentTheme -> MaterialTheme.colors.primary
                            theme == Theme.LightMode && theme == state.currentTheme -> MaterialTheme.colors.primary
                            theme == Theme.SystemSetting && theme == state.currentTheme -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.onPrimary
                        }
                    )
                }
            }
        }
    }
}