package com.example.mainichi.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mainichi.api.crypto.CryptoAPI
import com.example.mainichi.notifications.PriceNotification
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.*
import com.example.mainichi.ui.showNotificationScreen.ShowNotificationContract.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PriceChangeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val api: CryptoAPI
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        Log.d("Notification Test", "Doing my work for name: ${inputData.getString("asset")}")
        Log.d("Notification Test", "Doing my work for event: ${inputData.getString("event")}")
        Log.d("Notification Test", "Doing my work for value: ${inputData.getString("eventValue")}")

        val name = inputData.getString("asset") ?: ""
        val event = inputData.getString("event") ?: ""
        val eventValue = inputData.getString("eventValue") ?: ""

        val asset = api.getAllCryptoAssets().first { asset -> asset.name == name }

        val priceChangePercentage = asset.price_change_percentage_24h

        when {
            priceChangePercentage >= eventValue.toDouble() -> if (event == NotificationConfiguration.PriceEvent.PriceUp.toString()
                || event == NotificationConfiguration.PriceEvent.PriceUpDown.toString()
            ) {
                sendNotification(
                    text = "$name is up ${
                        String.format(
                            "%.2f",
                            priceChangePercentage
                        )
                    }% in the last 24h"

                )
            }
            priceChangePercentage < eventValue.toDouble() -> if (event == NotificationConfiguration.PriceEvent.PriceDown.toString()
                || event == NotificationConfiguration.PriceEvent.PriceUpDown.toString()
            ) {
                sendNotification(
                    text = "$name is down ${
                        String.format(
                            "%.2f",
                            priceChangePercentage
                        )
                    }% in the last 24h"

                )
            }
        }

        return Result.success()
    }

    private fun sendNotification(
        text: String
    ) {
        val notification = PriceNotification(applicationContext)
        notification.createNotificationChannel()
        notification.showNotification(
            text = text
        )
    }
}