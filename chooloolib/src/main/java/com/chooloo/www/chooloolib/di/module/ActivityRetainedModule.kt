package com.chooloo.www.chooloolib.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityRetainedProvidesModule

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedBindsModule
