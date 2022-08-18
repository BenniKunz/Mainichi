package com.example.mainichi.navigationDrawer

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainichi.ui.theme.MainichiTheme

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("My Nichi")
    }
}

@Composable
fun DrawerBody(
    items: List<DrawerItem>,
    modifier: Modifier = Modifier,
    onItemClick: (DrawerItem) -> Unit,
    onChangeTheme: () -> Unit,
    isDarkMode: Boolean
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
                    .clickable { onItemClick(item) },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    item.title,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {

            val checkedState = remember { mutableStateOf(isDarkMode) }


            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(text = "Dark Mode")

                Switch(
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        onChangeTheme()
                    })
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDrawer() {

    MainichiTheme() {
        DrawerBody(items = listOf(
            DrawerItem(
                type = ScreenType.News,
                title = "News",
                contentDescription = "News",
                icon = Icons.Filled.Favorite
            ),
            DrawerItem(
                type = ScreenType.Crypto,
                title = "Crypto",
                contentDescription = "Crypto",
                icon = Icons.Filled.Info
            )
        ), onItemClick = {},
            onChangeTheme = {},
            isDarkMode = true)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDrawerHeader() {
    MainichiTheme {
        DrawerHeader(

        )

    }

}