package com.example.mainichi.ui.createNotificationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mainichi.ui.ImageLoader

@Composable
fun NotificationsCard(
    notificationConfiguration: CreateNotificationContract.NotificationConfiguration
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.2f)
        ) {
            ImageLoader(
                data = notificationConfiguration.assets.first().image,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.6f),
            verticalArrangement = Arrangement.Center
        ) {

            Text(notificationConfiguration.assets.first().symbol.uppercase())

            Text(
                text = notificationConfiguration.date,
                style = MaterialTheme.typography.caption
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(notificationConfiguration.eventType.toString())

            Text(
                text = "every ${notificationConfiguration.intervalValue} ${notificationConfiguration.intervalType.asString()}",
                style = MaterialTheme.typography.caption
            )
        }
    }
}