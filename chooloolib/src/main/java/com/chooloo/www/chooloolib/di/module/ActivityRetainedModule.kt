package com.chooloo.www.chooloolib.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module(includes = [ActivityRetainedModule.BindsModule::class])
class ActivityRetainedModule {
    @Module
    @InstallIn(ActivityRetainedComponent::class)
    interface BindsModule
}