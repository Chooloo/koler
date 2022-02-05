package com.chooloo.www.kontacts.di.module

import com.chooloo.www.kontacts.di.factory.controllerfactory.ControllerFactory
import com.chooloo.www.kontacts.di.factory.controllerfactory.ControllerFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module(includes = [ActivityModule.BindsModule::class])
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Module
    @InstallIn(ActivityComponent::class)
    interface BindsModule {
        @Binds
        fun bindControllerFactory(controllerFactoryImpl: ControllerFactoryImpl): ControllerFactory
    }
}