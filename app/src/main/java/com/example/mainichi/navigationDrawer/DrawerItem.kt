package com.example.mainichi.navigationDrawer

import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenType {
    Crypto, News
}

data class DrawerItem(
    val type: ScreenType,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)