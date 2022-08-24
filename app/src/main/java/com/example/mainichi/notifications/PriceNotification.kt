package com.example.mainichi.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.customview.R
import androidx.work.ForegroundInfo
import kotlin.random.Random

class PriceNotification(
    val context: Context
) {

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "price_channel",
                "Price Alert",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    fun showNotification(
         asset : String,
         priceChangePercentage : Double
    ) {

        val builder = NotificationCompat.Builder(context, "price_channel")
            .setSmallIcon(R.drawable.notification_bg)
            .setContentText(if(priceChangePercentage >= 0) {
                "$asset is up $priceChangePercentage% in the last 24h"
            }else {
                "$asset is down $priceChangePercentage% in the last 24h\""
            })
            .setContentTitle("Price Change Alert")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())
        }
    }
}