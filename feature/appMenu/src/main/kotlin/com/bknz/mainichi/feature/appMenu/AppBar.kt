package com.bknz.mainichi.feature.appMenu

import android.content.res.Configuration
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bknz.mainichi.core.designsystem.MainichiTheme
//import com.example.mainichi.feature.appMenu.R

@Composable
fun MainichiAppBar(
    onToggleDrawer: () -> Unit,
    onNavigateToSettings: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = "Hello",//stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        navigationIcon = {

            IconButton(onClick = onToggleDrawer) {

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open menu",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = onNavigateToSettings) {
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
        MainichiAppBar({}) {

        }
    }

}