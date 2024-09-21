package com.chooloo.www.koler.di.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindsModule
