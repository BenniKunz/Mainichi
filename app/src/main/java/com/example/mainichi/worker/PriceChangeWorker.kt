package com.example.mainichi.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mainichi.api.crypto.CryptoAPI
import com.example.mainichi.notifications.PriceNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PriceChangeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val api: CryptoAPI
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        Log.d("Notification Test", "Doing my work for ${inputData.getString("asset")}")

        val asset = api.getAllCryptoAssets().first { asset -> asset.name == inputData.getString("asset") }

        sendNotification(
            asset = asset.name,
            priceChangePercentage = asset.price_change_percentage_24h
        )

        return Result.success()
    }

    private fun sendNotification(
        asset : String,
        priceChangePercentage : Double
    ) {
        val notification = PriceNotification(applicationContext)
        notification.createNotificationChannel()
        notification.showNotification(
            asset = asset,
            priceChangePercentage = priceChangePercentage
        )
    }
}