package com.WM.runny.presentation.RunFragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.WM.runny.R
import com.WM.runny.common.Constans.REQUEST_CODE_LOCATION_PERMISSION
import com.WM.runny.common.TrackingUtility
import com.WM.runny.data.RunAdapter.RunAdapter
import com.WM.runny.data.repository.SortType
import com.WM.runny.presentation.MainScreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragmet_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment:Fragment(R.layout.fragmet_run),EasyPermissions.PermissionCallbacks {
    private val viewModel:MainViewModel by viewModels()

    private lateinit var runAdapter:RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setupRecyclerView()

        when(viewModel.sortType){
            SortType.DATE ->spFilter.setSelection(0)
            SortType.RUNNING_TIME ->spFilter.setSelection(1)
            SortType.DISTANCE ->spFilter.setSelection(2)
            SortType.AVG_SPEED ->spFilter.setSelection(3)
            SortType.CALORIES_BURNED ->spFilter.setSelection(4)
        }

        spFilter.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(AdapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->viewModel.SortRuns(SortType.DATE)
                    1->viewModel.SortRuns(SortType.RUNNING_TIME)
                    2->viewModel.SortRuns(SortType.DISTANCE)
                    3->viewModel.SortRuns(SortType.AVG_SPEED)
                    4->viewModel.SortRuns(SortType.CALORIES_BURNED)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })

        fab.setOnClickListener{
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun setupRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermission(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept permission to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept permission to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}