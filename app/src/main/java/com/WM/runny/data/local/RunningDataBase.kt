package com.WM.runny.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.WM.runny.domain.run.Run

@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RunningDataBase:RoomDatabase() {
    abstract fun getRunDao():RunDao
}