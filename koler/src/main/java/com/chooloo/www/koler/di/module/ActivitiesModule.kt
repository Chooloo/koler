package com.chooloo.www.koler.di.module

import android.content.Context
import com.chooloo.www.koler.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class ActivitiesModule {
    @Provides
    fun provideMainActivity(@ActivityContext activity: Context): MainActivity {
        return activity as MainActivity
    }
}