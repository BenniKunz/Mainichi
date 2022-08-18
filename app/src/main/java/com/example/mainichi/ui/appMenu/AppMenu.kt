package bknz.example.mainichi.ui.appMenu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mainichi.navigationDrawer.DrawerData
import com.example.mainichi.navigationDrawer.ScreenType
import com.example.mainichi.navigationDrawer.appMenuData

@Composable
fun AppMenu(
    onItemClick: (ScreenType) -> Unit,
    onNavigateUpRequested: () -> Unit,
    onChangeTheme: () -> Unit,
    isDarkMode: Boolean
) {

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.0f))
            .size(300.dp, 500.dp)
            .clickable {
                onNavigateUpRequested()
            }
    ) {

        MainMenuBox(
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(64.dp))
                .border(2.dp, Color.White, RoundedCornerShape(64.dp))
                .background(MaterialTheme.colors.onBackground)
                .size(80.dp, 150.dp)
                .clickable {
                    onItemClick(ScreenType.Crypto)
                }, data = appMenuData[ScreenType.Crypto]
        )

        MainMenuBox(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(64.dp))
                .border(2.dp, Color.White, RoundedCornerShape(64.dp))
                .background(MaterialTheme.colors.primaryVariant)
                .size(80.dp, 180.dp)
                .clickable {
                    onItemClick(ScreenType.News)
                }, data = appMenuData[ScreenType.News]
        )


        MainMenuBox(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(64.dp))
                .border(2.dp, Color.White, RoundedCornerShape(64.dp))
                .background(MaterialTheme.colors.secondaryVariant)
                .size(80.dp, 120.dp),
            data = null
        )

        MainMenuSettingsBox(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(Color.White.copy(alpha = 0.0f))
                .size(160.dp, 200.dp),
            isDarkMode = isDarkMode,
            onChangeTheme = onChangeTheme
        )
    }
}


@Composable
fun MainMenuSettingsBox(
    modifier: Modifier,
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit,
) {

    val checkedState = remember { mutableStateOf(isDarkMode) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Dark Mode", modifier = Modifier.padding(end = 8.dp))

                Switch(
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        onChangeTheme()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary,
                        uncheckedThumbColor = MaterialTheme.colors.primaryVariant,
                        checkedTrackColor = MaterialTheme.colors.onBackground,
                        uncheckedTrackColor = MaterialTheme.colors.onBackground
                    )
                )
            }
        }
    }
}

@Composable
fun MainMenuBox(
    modifier: Modifier,
    data: DrawerData?
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = data?.title ?: "",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary
            )
            Icon(
                imageVector = data?.icon ?: Icons.Default.Favorite,
                contentDescription = data?.description,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}