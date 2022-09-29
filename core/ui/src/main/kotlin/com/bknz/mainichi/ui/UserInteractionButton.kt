package com.bknz.mainichi.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserInteractionButton(text: String, modifier: Modifier, onClick: () -> Unit) {

    Button(
        onClick = { onClick() },
//        modifier = modifier,
        shape = RoundedCornerShape(60.dp)
    ) {
        Text(text = text)
    }
}