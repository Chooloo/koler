package com.chooloo.www.chooloolib.di

import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.data.repository.audio.AudioRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.blocked.BlockedRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.clipboard.ClipboardRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.contact.ContactRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.navigation.NavigationRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.permission.PermissionRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.phone.PhoneRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.preference.PreferenceRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.rawcontact.RawContactRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.recent.RecentRepositoryImpl
import com.chooloo.www.chooloolib.data.repository.telecom.TelecomRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.blocked.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.navigation.NavigationRepository
import com.chooloo.www.chooloolib.domain.repository.permission.PermissionRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.rawcontact.RawContactRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationProvidesModule {
    private const val SHARED_PREFERENCES_NAME = "chooloo_preferences"

    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    fun provideTelecomManager(@ApplicationContext context: Context): TelecomManager =
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    @Provides
    fun provideTelephonyManager(@ApplicationContext context: Context): TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat =
        NotificationManagerCompat.from(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindsModule {
    @Binds
    abstract fun bindPhoneRepository(impl: PhoneRepositoryImpl): PhoneRepository

    @Binds
    abstract fun bindAudioRepository(impl: AudioRepositoryImpl): AudioRepository

    @Binds
    abstract fun bindRecentRepository(impl: RecentRepositoryImpl): RecentRepository

    @Binds
    abstract fun bindTelecomRepository(impl: TelecomRepositoryImpl): TelecomRepository

    @Binds
    abstract fun bindBlockedRepository(impl: BlockedRepositoryImpl): BlockedRepository

    @Binds
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository

    @Binds
    abstract fun bindClipboardRepository(impl: ClipboardRepositoryImpl): ClipboardRepository

    @Binds
    abstract fun bindPermissionRepository(impl: PermissionRepositoryImpl): PermissionRepository

    @Binds
    abstract fun bindNavigationRepository(impl: NavigationRepositoryImpl): NavigationRepository

    @Binds
    abstract fun bindPreferenceRepository(impl: PreferenceRepositoryImpl): PreferenceRepository

    @Binds
    abstract fun bindRawContactRepository(impl: RawContactRepositoryImpl): RawContactRepository
}
