package com.example.mainichi.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.*
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.NotificationConfiguration.*
import com.example.mainichi.ui.entities.Asset

@Entity(tableName = "notification")
data class DbNotification(
    @PrimaryKey
    val tag: String,
    val name: String,
    val image: String,
    val symbol: String,
    val event: String,
    val eventValue: String,
    val intervalType: String,
    val interval: String,
    val date: String,
)


fun DbNotification.toNotificationConfiguration() = NotificationConfiguration(
    assets = listOf(
        Asset(
            name = this.name,
            symbol = this.symbol,
            image = this.image
        )
    ),
    eventType = PriceEvent.valueOf(this.event),
    eventValue = this.eventValue,
    intervalValue = this.interval,
    intervalType = Periodically.valueOf(this.intervalType),
    date = this.date
    )

