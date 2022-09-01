package com.WM.runny.presentation.StatisticFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.WM.runny.R
import com.WM.runny.presentation.MainScreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment:Fragment(R.layout.statistic_fragmet) {
    private val viewModel: StatisticViewModel by viewModels()
}