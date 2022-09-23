package com.example.mainichi.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingStateProgressIndicator(color : Color = MaterialTheme.colors.background, size : Int = 20) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size.dp),
            strokeWidth = 5.dp,
            color = color)
    }
}