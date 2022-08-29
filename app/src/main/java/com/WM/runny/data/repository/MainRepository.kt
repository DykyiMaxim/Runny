package com.WM.runny.data.repository

import com.WM.runny.data.local.RunDao
import com.WM.runny.domain.run.Run
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun insertRun(run: Run)=runDao.insertRun(run)

    suspend fun deleteRun(run: Run)=runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunSortedByDate()

    fun getAllRunsSortedByTimeInMills() = runDao.getAllRunSortedByTimeInMills()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunSortedByCaloriesBurned()

    fun getAllRunsSortedByDistance() = runDao.getAllRusSortedByDistance()

    fun getTotalDistance()=runDao.getTotalDistanceInMeters()

    fun getTotalAvgSpeed()=runDao.getTotalAvgSpeed()

    fun getTotalCaloriesBurned()=runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMills()=runDao.getTotalTimeInMills()
}