package com.chooloo.www.kontacts.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module(includes = [ActivityModule.BindsModule::class])
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Module
    @InstallIn(ActivityComponent::class)
    interface BindsModule {
    }
}