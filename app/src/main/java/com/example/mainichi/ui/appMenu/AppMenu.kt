package com.example.mainichi.ui.appMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mainichi.navigationDrawer.DrawerItem
import com.example.mainichi.navigationDrawer.ScreenType
import com.example.mainichi.navigationDrawer.appMenuData

@Composable
fun AppMenu(
    onItemClick: (ScreenType) -> Unit,
    onChangeTheme: () -> Unit,
    isDarkMode: Boolean
) {

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.0f))
            .size(300.dp, 500.dp)
    ) {

        Box(modifier = Modifier
            .size(100.dp, 300.dp)
            .align(Alignment.TopStart)
            .clip(RoundedCornerShape(64.dp))
            .background(MaterialTheme.colors.onBackground)
            .clickable {
                onItemClick(ScreenType.Crypto)
            },
        contentAlignment = Alignment.Center) {

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                val data = appMenuData[ScreenType.Crypto]

                Text(
                    text = data?.title ?: "provide name",
                    color = MaterialTheme.colors.primary)
                Icon(
                    imageVector = data?.icon ?: Icons.Default.Favorite,
                    contentDescription = data?.description,
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        Box(modifier = Modifier
            .size(100.dp, 200.dp)
            .align(Alignment.TopEnd)
            .clip(RoundedCornerShape(64.dp))
            .background(MaterialTheme.colors.primaryVariant)
            .clickable {
                onItemClick(ScreenType.News)
            }, contentAlignment = Alignment.Center) {

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                val data = appMenuData[ScreenType.News]

                Text(text = data?.title ?: "provide name")
                Icon(
                    imageVector = data?.icon ?: Icons.Default.Favorite,
                    contentDescription = data?.description,
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        Box(
            modifier = Modifier
                .size(100.dp, 200.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(64.dp))
                .background(MaterialTheme.colors.onBackground)
        )

    }

//    LazyColumn(
//        modifier = Modifier
//            .background(MaterialTheme.colors.primary),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        items(drawerData) { item ->
//
//            Row(
//                modifier = Modifier
//                    .padding(all = 8.dp)
//                    .clickable { onItemClick(item) },
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = item.contentDescription
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//
//                Text(
//                    item.title
//                )
//            }
//        }
//
//        item {
//
//            val checkedState = remember { mutableStateOf(isDarkMode) }
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//
//                Text(text = "Dark Mode")
//
//                Switch(
//                    checked = checkedState.value,
//                    onCheckedChange = {
//                        checkedState.value = it
//                        onChangeTheme()
//                    })
//            }
//        }
//    }

}