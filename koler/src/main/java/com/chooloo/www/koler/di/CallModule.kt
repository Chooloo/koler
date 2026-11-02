package com.chooloo.www.koler.di

import com.chooloo.www.koler.data.CallRepositoryImpl
import com.chooloo.www.koler.domain.CallRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CallModule {
    @Binds
    abstract fun bindCallRepository(impl: CallRepositoryImpl): CallRepository
}
