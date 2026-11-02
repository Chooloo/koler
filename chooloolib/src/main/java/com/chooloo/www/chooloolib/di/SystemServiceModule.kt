package com.chooloo.www.chooloolib.di

import android.content.ContentResolver
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            migrations = listOf(SharedPreferencesMigration(context, LEGACY_PREFERENCES_NAME)),
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) }
        )

    private const val PREFERENCES_NAME = "settings"
    private const val LEGACY_PREFERENCES_NAME = "chooloo_preferences"
}
