package com.WM.runny.common

import android.graphics.Color

object Constans {
    const val RUNNING_DATABASE_NAME = "running_db"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val LOCATION_UPDATE_MILLS = 50000L
    const val FASTEST_LOCATION_INTERVAL = 20000L
    const val TIMER_UPDATE_INTERVAL = 50L
    const val CANCEL_TRACKING_DIALOG_TAG = "CANCEL_TRACKING_DIALOG_TAG"


    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val NOTIFICATION_CHANNEL_ID = "Tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

}