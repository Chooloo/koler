package com.chooloo.www.chooloolib.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.data.repository.AudioRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.BlockedRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.ContactAccountRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.ContactRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.PhoneRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.PreferenceRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.RecentRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.TelecomRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.ContactAccountRepository
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemServiceModule {
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver = context.contentResolver

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager =
        context.getSystemService(AudioManager::class.java)

    @Provides
    fun provideTelecomManager(@ApplicationContext context: Context): TelecomManager =
        context.getSystemService(TelecomManager::class.java)

    @Provides
    fun provideTelephonyManager(@ApplicationContext context: Context): TelephonyManager =
        context.getSystemService(TelephonyManager::class.java)

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            context.getSystemService(Vibrator::class.java)
        }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("chooloo_preferences", Context.MODE_PRIVATE)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAudioRepository(impl: AudioRepositoryImpl): AudioRepository

    @Binds
    abstract fun bindBlockedRepository(impl: BlockedRepositoryImpl): BlockedRepository

    @Binds
    abstract fun bindContactAccountRepository(impl: ContactAccountRepositoryImpl): ContactAccountRepository

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
