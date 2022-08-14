package com.example.mainichi.navigationDrawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerData(
    val title: String,
    val description: String,
    val icon : ImageVector
)

//val drawerData = listOf(
//    DrawerItem(ScreenType.Home, "Home", "Go to the home screen", Icons.Default.Home),
//    DrawerItem(ScreenType.News, "News", "Go to the news screen", Icons.Default.Create),
//    DrawerItem(ScreenType.Crypto, "Crypto", "Go to the crypto screen", Icons.Default.Favorite),
//    DrawerItem(ScreenType.Sports, "Sports", "Go to the sports screen", Icons.Default.Favorite),
//)

val appMenuData = mapOf(
    ScreenType.Crypto to DrawerData(
        title = "Home",
        description = "Go to the home screen",
        icon = Icons.Default.Home
    ),
    ScreenType.News to DrawerData(
        title = "News",
        description = "Go to the news screen",
        icon = Icons.Default.Create
    ),
    ScreenType.Sports to DrawerData(
        title = "Sports",
        description = "Go to the sports screen",
        icon = Icons.Default.Favorite
    ),
)