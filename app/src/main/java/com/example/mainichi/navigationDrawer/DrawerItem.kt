package com.example.mainichi.navigationDrawer

import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenType{
    Home, Crypto, News, Sports
}

data class DrawerItem(
    val type : ScreenType,
    val title : String,
    val contentDescription : String,
    val icon : ImageVector
)