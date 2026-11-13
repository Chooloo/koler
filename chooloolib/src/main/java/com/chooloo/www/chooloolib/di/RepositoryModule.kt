package com.chooloo.www.chooloolib.di

import com.chooloo.www.chooloolib.data.repository.AudioRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.BlockedRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.ContactRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.PhoneRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.PreferenceRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.RecentRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.TelecomRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAudioRepository(impl: AudioRepositoryImpl): AudioRepository

    @Binds
    abstract fun bindBlockedRepository(impl: BlockedRepositoryImpl): BlockedRepository

    @Binds
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository

    @Binds
    abstract fun bindPhoneRepository(impl: PhoneRepositoryImpl): PhoneRepository

    @Binds
    abstract fun bindPreferenceRepository(impl: PreferenceRepositoryImpl): PreferenceRepository

    @Binds
    abstract fun bindRecentRepository(impl: RecentRepositoryImpl): RecentRepository

    @Binds
    abstract fun bindTelecomRepository(impl: TelecomRepositoryImpl): TelecomRepository
}
