package com.WM.runny.data.TrackingService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.WM.runny.R
import com.WM.runny.common.Constans.ACTION_PAUSE_SERVICE
import com.WM.runny.common.Constans.ACTION_START_OR_RESUME_SERVICE
import com.WM.runny.common.Constans.ACTION_STOP_SERVICE
import com.WM.runny.common.Constans.NOTIFICATION_CHANNEL_ID
import com.WM.runny.common.Constans.NOTIFICATION_CHANNEL_NAME
import com.WM.runny.common.Constans.ACTION_SHOW_TRACKING_FRAGMENT
import com.WM.runny.common.Constans.FASTEST_LOCATION_INTERVAL
import com.WM.runny.common.Constans.LOCATION_UPDATE_MILLS
import com.WM.runny.common.Constans.NOTIFICATION_ID
import com.WM.runny.common.TrackingUtility
import com.WM.runny.presentation.MainScreen.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService:LifecycleService() {
    var isFirstRun = true
    lateinit var fuseLocationProviderClient:FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fuseLocationProviderClient  = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    private fun startForegroundService(){
        addEmptyPolyLine()
        isTracking.postValue(true)

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
            .setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun=false
                    }else{Timber.d("Resuming Service... ")}
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

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this@TrackingService,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT

    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crateNotificationChannel(notificationManager: NotificationManager){

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    private fun addPathPoint(location:Location?){
        location?.let {
            val position = LatLng(location.latitude,location.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
        }
    }

    val locationCallback = object :LocationCallback(){

        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)

            if(isTracking.value!!){
                result?.locations?.let{locations->
                    for(location in locations){
                        addPathPoint(location)
                        Timber.d("NEW_LOCATION: ${location.latitude}, ${location.longitude}")
                    }

                }
            }
        }
    }

    private fun updateLocationTracking(isTracking:Boolean){
        if(isTracking){
            if(TrackingUtility.hasLocationPermission(this)){
                val request = com.google.android.gms.location.LocationRequest().apply {
                    interval = LOCATION_UPDATE_MILLS
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY

                }
                fuseLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }else{
            fuseLocationProviderClient.removeLocationUpdates(locationCallback)
        }

    }

    private fun addEmptyPolyLine()= pathPoints.value?.apply {
        pathPoints.postValue(this)
    }?: pathPoints.postValue(mutableListOf(mutableListOf()))

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }
}