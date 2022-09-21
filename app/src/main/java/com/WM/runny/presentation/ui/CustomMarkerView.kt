package com.WM.runny.presentation.ui

import android.content.Context
import com.WM.runny.common.TrackingUtility
import com.WM.runny.domain.run.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs:List<Run>,
    c: Context,
    layoutId:Int
) :MarkerView(c,layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width/2f, -height/2f)
    }



    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val currentRunId = e.x.toInt()
        val run = runs[currentRunId]


        val callendar = Calendar.getInstance().apply {
            timeInMillis = run.timestampInMills
        }
        val datFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tvDate.text = datFormat.format(callendar.time)

        val avgSpeed = "${run.avgSpeed}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceKm = "${run.distanceInMeters / 1000f}km"
        tvDistance.text = distanceKm

        tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMills)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }
}