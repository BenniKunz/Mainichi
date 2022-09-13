package com.example.mainichi.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Pink,
    primaryVariant = Cyan,
    secondary = Color.White,
    secondaryVariant = Color.Black,
    background = Ebony,
    onBackground = Slate,
    onSurface = Color.White,
    onPrimary = Pewter,
    onSecondary = Gray
)

private val LightColorPalette = lightColors(
    primary = Red,
    primaryVariant = Teal,
    secondary = Color.Black,
    secondaryVariant = Color.White,
    background = SandDollar,
    onBackground = NavyBlue,
    onSurface = Color.White,
    onPrimary = Pewter,
    onSecondary = Gray
)

@Composable
fun MainichiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}