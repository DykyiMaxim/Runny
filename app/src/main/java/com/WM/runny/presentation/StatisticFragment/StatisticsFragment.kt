package com.WM.runny.presentation.StatisticFragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.WM.runny.R
import com.WM.runny.common.TrackingUtility
import com.WM.runny.presentation.MainScreen.MainViewModel
import com.WM.runny.presentation.ui.CustomMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.statistic_fragmet.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment:Fragment(R.layout.statistic_fragmet) {
    private val viewModel: StatisticViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBar()
    }

    private fun setupBar(){
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor= Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)

        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }

    }



    private fun subscribeToObservers(){
        viewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let{
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTimeRun
            }
        })
        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let{
                val km = it/1000f
                val totalDistance = round(km*10f)/10f
                val totalDistanceString = "${totalDistance} km"
                tvTotalDistance.text = totalDistanceString
            }
        })
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let{
                val avgSpeed = round(it*10f)/10f
                val avgSpeedString = "${avgSpeed} km/h"
                tvAverageSpeed.text = avgSpeedString
            }
        })
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
            it?.let{
                val totalCaloriesBurned = "${it}kcal"
                tvTotalCalories.text = totalCaloriesBurned
            }
        })
        viewModel.runsSoretedByDate.observe(viewLifecycleOwner, Observer {
            it?.let{
                val allAvgSpeeds = it.indices.map { i->BarEntry(i.toFloat(),it[i].avgSpeed) }
                val BarDataSet  =BarDataSet(allAvgSpeeds,"avg speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(),R.color.colorAccent)
                }
                barChart.data = BarData(BarDataSet)
                barChart.marker = CustomMarkerView(it.reversed(),requireContext(),R.layout.marker_view)
                barChart.invalidate()
        }
        })


    }

}