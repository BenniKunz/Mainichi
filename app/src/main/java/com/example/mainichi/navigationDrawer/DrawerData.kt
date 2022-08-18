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
    )
)