package com.chooloo.www.koler.di

import com.chooloo.www.koler.data.CallRepositoryImpl
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.service.CallService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.Nullable

@Module
@InstallIn(SingletonComponent::class)
abstract class CallModule {
    @Binds
    abstract fun bindCallRepository(impl: CallRepositoryImpl): CallRepository

    companion object {
        @Provides
        @Nullable
        fun provideCallService(): CallService? = CallService.sInstance
    }
}
