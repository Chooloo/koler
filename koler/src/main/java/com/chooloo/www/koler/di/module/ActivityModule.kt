package com.chooloo.www.koler.di.module

import com.chooloo.www.chooloolib.di.module.ActivityModule
import com.chooloo.www.koler.di.factory.controller.ControllerFactory
import com.chooloo.www.koler.di.factory.controller.ControllerFactoryImpl
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