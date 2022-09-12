package com.WM.runny.data.TrackingService

import android.annotation.SuppressLint
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
import com.WM.runny.common.Constans.TIMER_UPDATE_INTERVAL
import com.WM.runny.common.TrackingUtility
import com.WM.runny.presentation.MainScreen.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    var isFirstRun = true
    var serviceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var baseNotificationBuilder:NotificationCompat.Builder

    lateinit var currentNotificationBuilder:NotificationCompat.Builder



    private val timeRunInSeconds = MutableLiveData<Long>()


    companion object {
        val timeRunInMiles = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues() {
        timeRunInMiles.postValue(0L)
        timeRunInSeconds.postValue(0L)
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    private fun killService(){
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                        StartTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var   lapTime = 0L
    private var TimeRun = 0L
    private var timeStarted = 0L
    private var lastSecndTimeStamp = 0L

    private fun StartTimer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {
            while(isTracking.value!!){
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInMiles.postValue(TimeRun+lapTime)
                if(timeRunInMiles.value!! >= lastSecndTimeStamp+1000L){
                    timeRunInSeconds.postValue(timeRunInSeconds.value!!+1)
                    lastSecndTimeStamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            TimeRun += lapTime
        }

    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean){
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking){
            val pauseIntent = Intent(this,TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1,pauseIntent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        }else{
           val resumeIntent = Intent(this,TrackingService::class.java).apply {
               action = ACTION_START_OR_RESUME_SERVICE
           }
            PendingIntent.getService(this,2,resumeIntent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if(!serviceKilled){
        currentNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.ic_pause_black_24dp, notificationActionText,pendingIntent)
        notificationManager.notify(NOTIFICATION_ID,currentNotificationBuilder.build())}
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if(TrackingUtility.hasLocationPermission(this)) {
                val request = com.google.android.gms.location.LocationRequest().apply {
                    interval = LOCATION_UPDATE_MILLS
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

     val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!) {
                result?.locations?.let { locations ->
                    for(location in locations) {
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            super.onLocationAvailability(p0)
        }
    }



    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        StartTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
        timeRunInSeconds.observe(this, Observer {
            if(!serviceKilled){
                val notfication = currentNotificationBuilder
                    .setContentText(TrackingUtility.getFormattedStopWatchTime(it*1000L))
                notificationManager.notify(NOTIFICATION_ID,notfication.build())
            }

        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}