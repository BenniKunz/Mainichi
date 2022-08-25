package com.example.mainichi.db

import androidx.room.Entity
import androidx.room.PrimaryKey

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