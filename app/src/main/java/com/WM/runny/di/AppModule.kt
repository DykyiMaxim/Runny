package com.WM.runny.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.WM.runny.BaseApplication
import com.WM.runny.common.Constans.KEY_FIRST_TIME_TOGGLE
import com.WM.runny.common.Constans.KEY_NAME
import com.WM.runny.common.Constans.KEY_WEIGHT
import com.WM.runny.common.Constans.RUNNING_DATABASE_NAME
import com.WM.runny.common.Constans.SHEARED_PREFERENCES_NAME
import com.WM.runny.data.local.RunningDataBase
import dagger.Binds
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
        RUNNING_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRunDao(db:RunningDataBase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharePreverences(
        @ApplicationContext app:Context
    ) = app.getSharedPreferences(SHEARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref:SharedPreferences) = sharedPref.getString(KEY_NAME,"")?:""

    @Singleton
    @Provides
    fun provideWeight(sharedPref:SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT,80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref:SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE,true)


}