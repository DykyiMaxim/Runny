package com.WM.runny.domain.run

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run (
    var img:Bitmap? = null,
    var timestampInMills:Long = 0L,
    var avgSpeed:Float = 0f,
    var distanceInMeters:Int = 0,
    var timeInMills:Long = 0L,
    var caloriesBurned:Int=0
        ){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}