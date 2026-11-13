package com.chooloo.www.koler.di

import com.chooloo.www.koler.data.repository.CallRepositoryImpl
import com.chooloo.www.koler.domain.repository.CallRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCallRepository(impl: CallRepositoryImpl): CallRepository
}
