package com.chooloo.www.koler.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityProvidesModule

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityBindsModule
