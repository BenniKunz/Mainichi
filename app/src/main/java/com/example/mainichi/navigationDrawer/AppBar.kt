package com.example.mainichi.navigationDrawer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mainichi.R
import com.example.mainichi.ui.theme.MainichiTheme


@Composable
fun MainichiAppBar(
    isDetailScreen: Boolean,
    onToggleDrawer: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToSettings: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        navigationIcon = if (!isDetailScreen) {
            {
                IconButton(onClick = onToggleDrawer) {

                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open menu",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }
        } else {
            {
                IconButton(onClick = onNavigateUp) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onNavigateToSettings ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "settings",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppBar() {
    MainichiTheme() {
        MainichiAppBar(isDetailScreen = false, {}, {}) {

        }
    }

}