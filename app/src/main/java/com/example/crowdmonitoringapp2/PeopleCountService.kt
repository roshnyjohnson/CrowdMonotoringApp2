package com.example.crowdmonitoringapp2

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat

class PeopleCountService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.createChannel(this)

        val notification = NotificationCompat.Builder(this, NotificationUtils.CHANNEL_ID)
            .setContentTitle("Crowd Monitoring")
            .setContentText("People counting running in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        startForeground(1, notification)

        runnable = Runnable {
            // 🔹 This simulates counting people every 30 seconds
            // Later replace this with ML Kit logic
            println("Counting people...")

            handler.postDelayed(runnable, 30_000)
        }

        handler.post(runnable)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
