package com.example.mainichi.helper.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mainichi.notifications.PriceNotification

class PriceChangeWorker(
    private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        Log.d("Notification Test", "Doing my work")
        sendNotification()

        return Result.success()
    }

    private fun sendNotification() {
        val notification = PriceNotification(appContext)
        notification.createNotificationChannel()
        notification.showNotification()
    }
}