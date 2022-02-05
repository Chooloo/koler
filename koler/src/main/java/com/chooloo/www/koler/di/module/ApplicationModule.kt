package com.chooloo.www.koler.di.module

import com.chooloo.www.koler.di.factory.fragment.FragmentFactory
import com.chooloo.www.koler.di.factory.fragment.FragmentFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun bindFragmentsFactory(fragmentsFactoryImpl: FragmentFactoryImpl): FragmentFactory
    }
}