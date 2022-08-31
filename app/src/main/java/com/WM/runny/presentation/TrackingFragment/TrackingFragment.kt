package com.WM.runny.presentation.TrackingFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.WM.runny.R
import com.WM.runny.presentation.MainScreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment:Fragment(R.layout.fragment_tracking) {
    private val viewModel: MainViewModel by viewModels()
}