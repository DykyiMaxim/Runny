package com.WM.runny.presentation.RunFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.WM.runny.R
import com.WM.runny.common.TrackingUtility
import com.WM.runny.presentation.MainScreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment:Fragment(R.layout.fragmet_run) {
    private val viewModel:MainViewModel by viewModels()


    private fun RequestPermissiom(){
        if(TrackingUtility.hasLocationPermission(requireContext())){
            return

        }

    }
}