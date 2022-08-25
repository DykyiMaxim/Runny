package com.WM.runny.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.WM.runny.domain.run.Run

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestampInMills DESC")
    fun getAllRunSortedByDate():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeInMills DESC")
    fun getAllRunSortedByTimeInMills():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    fun getAllRunSortedByCaloriesBurned():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeed DESC")
    fun getAllRunSortedByAvgSpeed():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
    fun getAllRusSortedByDistance():LiveData<List<Run>>

    @Query("SELECT SUM(timeInMills) FROM running_table")
    fun getTotalTimeInMills():LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    fun getTotalCaloriesBurned():LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    fun getTotalDistanceInMeters():LiveData<Long>

    @Query("SELECT AVG(avgSpeed) FROM running_table")
    fun getTotalAvgSpeed():LiveData<Float>
}