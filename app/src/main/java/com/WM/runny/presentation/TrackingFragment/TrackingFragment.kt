package com.WM.runny.presentation.TrackingFragment

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.WM.runny.R
import com.WM.runny.common.Constans.ACTION_PAUSE_SERVICE
import com.WM.runny.common.Constans.ACTION_START_OR_RESUME_SERVICE
import com.WM.runny.common.Constans.MAP_ZOOM
import com.WM.runny.common.Constans.POLYLINE_COLOR
import com.WM.runny.common.Constans.POLYLINE_WIDTH
import com.WM.runny.data.TrackingService.PolyLine
import com.WM.runny.data.TrackingService.TrackingService
import com.WM.runny.data.TrackingService.TrackingService.Companion.pathPoints
import com.WM.runny.presentation.MainScreen.MainViewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*

@AndroidEntryPoint
class TrackingFragment:Fragment(R.layout.fragment_tracking) {
    private val viewModel: MainViewModel by viewModels()

    private var isTracking = false

    private var pathPoints = mutableListOf<PolyLine>()

    private var map:GoogleMap?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        btnToggleRun.setOnClickListener{
            toggleRun()
        }
        mapView.getMapAsync{map = it
            addAllPolyLines()

        }

        subscribeToObservers()
    }

    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(
            viewLifecycleOwner, Observer {
                updateTracking(it)

        })
        TrackingService.pathPoints.observe(viewLifecycleOwner,
        Observer{
            pathPoints=it
            addLatestPolyline()
            moveCameraToUser()

        })
    }

    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)

        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking (isTracking:Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            btnToggleRun.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        }else{
            btnToggleRun.text = "Stop"
            btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last()?.isNotEmpty()==true){
            CameraUpdateFactory.newLatLngZoom(
                pathPoints.last().last(),
                MAP_ZOOM

            )
        }
    }

    private fun addAllPolyLines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)

        }
    }

    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty()&& pathPoints.last().size >1){
            val preLastLayLng = pathPoints.last()[pathPoints.last().size -2]
            val LastlatLong = pathPoints.last().last()
            val polyLineOption = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLayLng)
                .add(LastlatLong)
           map?.addPolyline(polyLineOption)
        }
    }

    private fun sendCommandToService(action:String) =
        Intent(requireContext(),TrackingService::class.java).also {
            it.action = action
            requireContext().stopService(it)
        }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()

    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}
