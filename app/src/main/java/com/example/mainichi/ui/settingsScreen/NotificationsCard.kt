package com.example.mainichi.ui.settingsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader

@Composable
fun NotificationsCard(
    notification: AssetNotification
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageLoader(
            data = notification.image,
            modifier = Modifier.size(32.dp)
        )

        Column() {

            Text(notification.symbol.uppercase())

            Text(
                text = notification.date,
                style = MaterialTheme.typography.caption
            )
        }

        Column() {
            Text(notification.event)

            Text(
                text = "every ${notification.interval} ${notification.intervalType}",
                style = MaterialTheme.typography.caption
            )
        }
    }
}