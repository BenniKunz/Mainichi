package com.example.mainichi.helper.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mainichi.notifications.PriceNotification



class TestWorker(
    private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {


        Log.d("Notification", "Doing my work")
        sendNotification()

        return Result.success()
    }

    private fun sendNotification() {
        val notification = PriceNotification(appContext)
        notification.createNotificationChannel()
        notification.showNotification()
    }
}