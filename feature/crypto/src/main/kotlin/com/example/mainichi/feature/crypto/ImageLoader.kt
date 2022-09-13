package com.example.mainichi.feature.crypto

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize

@Composable
fun ImageLoader(
    data : String,
    noPictureColor : Color = MaterialTheme.colors.primary,
    modifier: Modifier = Modifier
        .aspectRatio(1f)) {

    val painter = rememberImagePainter(
        data = data,
        builder = { size(OriginalSize) })

    when (painter.state) {
        is ImagePainter.State.Empty -> EmptyState(noPictureColor)
        is ImagePainter.State.Loading -> LoadingStateProgressIndicator()
        is ImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = modifier,
            )
        }
        is ImagePainter.State.Error -> EmptyState(noPictureColor)
    }
}

@Composable
fun EmptyState(color : Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        Alignment.Center

    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            tint = color
        )
    }
}