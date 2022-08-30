package com.example.mainichi.ui.createNotificationScreen

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
notificationConfiguration: CreateNotificationContract.NotificationConfiguration
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageLoader(
            data = notificationConfiguration.assets.first().image,
            modifier = Modifier.size(32.dp)
        )

        Column() {

            Text(notificationConfiguration.assets.first().symbol.uppercase())

            Text(
                text = notificationConfiguration.date,
                style = MaterialTheme.typography.caption
            )
        }

        Column() {
            Text(notificationConfiguration.eventType.toString())

            Text(
                text = "every ${notificationConfiguration.intervalValue} ${notificationConfiguration.intervalType}",
                style = MaterialTheme.typography.caption
            )
        }
    }
}