package com.WM.runny.presentation.StatisticFragment

import androidx.lifecycle.ViewModel
import com.WM.runny.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    val mainRepository: MainRepository
) :ViewModel(){
    val totalTimeRun = mainRepository.getTotalTimeInMills()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()

    val runsSoretedByDate = mainRepository.getAllRunsSortedByDate()

}