package bknz.example.mainichi.navigationDrawer

import android.content.res.Configuration
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mainichi.R
import com.example.mainichi.ui.theme.MainichiTheme


@Composable
fun MainichiAppBar(
    onToggleDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary)
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(onClick = onToggleDrawer) {

                Icon(
                   imageVector = Icons.Default.Menu,
                   contentDescription = "Toggle Drawer",
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
        MainichiAppBar {

        }
    }

}