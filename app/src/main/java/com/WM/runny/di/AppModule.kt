package com.WM.runny.di

import android.content.Context
import androidx.room.Room
import com.WM.runny.BaseApplication
import com.WM.runny.common.Constans.RUNNING_DATABASE_NAME
import com.WM.runny.data.local.RunningDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context
    )= Room.databaseBuilder(
        app,
        RunningDataBase::class.java,
        RUNNING_DATABASE_NAME)
    .build()

    @Singleton
    @Provides
    fun provideRunDao(db:RunningDataBase) = db.getRunDao()


}