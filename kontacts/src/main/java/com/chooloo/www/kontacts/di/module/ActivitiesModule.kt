package com.chooloo.www.kontacts.di.module

import android.app.Activity
import com.chooloo.www.kontacts.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivitiesModule {
    @Provides
    fun provideMainActivity(activity: Activity): MainActivity {
        return activity as MainActivity
    }
}