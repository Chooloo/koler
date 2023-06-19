package com.chooloo.www.chooloolib.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
class ScopeModule {
    @DefaultDispatcher
    @Provides
    fun provideDefaultScope(@DefaultDispatcher defaultDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(defaultDispatcher)

    @IoScope
    @Provides
    fun provideIoScope(@IoDispatcher ioDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(ioDispatcher)

    @MainScope
    @Provides
    fun provideMainScope(@MainDispatcher mainDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(mainDispatcher)
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultScope

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoScope

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainScope