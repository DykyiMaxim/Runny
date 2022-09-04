package com.WM.runny.data.TrackingService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.WM.runny.R
import com.WM.runny.common.Constans.ACTION_PAUSE_SERVICE
import com.WM.runny.common.Constans.ACTION_START_OR_RESUME_SERVICE
import com.WM.runny.common.Constans.ACTION_STOP_SERVICE
import com.WM.runny.common.Constans.NOTIFICATION_CHANNEL_ID
import com.WM.runny.common.Constans.NOTIFICATION_CHANNEL_NAME
import com.WM.runny.common.Constans.NOTIFICATION_ID
import timber.log.Timber

class TrackingService:LifecycleService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    Timber.d("Started or resume service")
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause service")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop service")
                }

        }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun startForegroundService(){
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            crateNotificationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running app")
            .setContentText("00:00:00")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crateNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
        IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}