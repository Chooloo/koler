package com.chooloo.www.chooloolib.di.module

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.Context
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactory
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactoryImpl
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractorImpl
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractorImpl
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractorImpl
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractorImpl
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractorImpl
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractorImpl
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractorImpl
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractorImpl
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractorImpl
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractorImpl
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractorImpl
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractorImpl
import com.chooloo.www.chooloolib.util.PreferencesManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module(includes = [ApplicationModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
public class ApplicationModule {
    @Provides
    fun provideVibrator(@ApplicationContext context: Context) =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @Provides
    fun providePowerManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.POWER_SERVICE) as PowerManager

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    fun provideTelecomManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    @Provides
    fun provideKeyguardManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    fun provideInputMethodManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context) =
        PreferencesManager.getInstance(context)

    @Provides
    fun provideSubscriptionManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context) =
        NotificationManagerCompat.from(context)


    @Module
    interface BindsModule {
        @Binds
        fun bindLiveDataFactory(liveDataFactoryImpl: LiveDataFactoryImpl): LiveDataFactory

        @Binds
        fun bindCallsInteractor(callsInteractorImpl: CallsInteractorImpl): CallsInteractor

        @Binds
        fun bindColorsInteractor(colorsInteractorImpl: ColorsInteractorImpl): ColorsInteractor

        @Binds
        fun bindAudiosInteractor(audiosInteractorImpl: AudiosInteractorImpl): AudiosInteractor

        @Binds
        fun bindPhonesInteractor(phonesInteractorImpl: PhonesInteractorImpl): PhonesInteractor

        @Binds
        fun bindStringsInteractor(stringsInteractorImpl: StringsInteractorImpl): StringsInteractor

        @Binds
        fun bindBlockedInteractor(blockedInteractorImpl: BlockedInteractorImpl): BlockedInteractor

        @Binds
        fun bindRecentsInteractor(recentsInteractorImpl: RecentsInteractorImpl): RecentsInteractor

        @Binds
        fun bindContactsInteractor(contactsInteractorImpl: ContactsInteractorImpl): ContactsInteractor

        @Binds
        fun bindDrawablesInteractor(drawablesInteractorImpl: DrawablesInteractorImpl): DrawablesInteractor

        @Binds
        fun bindAnimationsInteractor(animationsInteractorImpl: AnimationsInteractorImpl): AnimationsInteractor

        @Binds
        fun bindCallAudiosInteractor(callAudiosInteractorImpl: CallAudiosInteractorImpl): CallAudiosInteractor

        @Binds
        fun bindProximitiesInteractor(proximitiesInteractorImpl: ProximitiesInteractorImpl): ProximitiesInteractor

        @Binds
        fun bindPreferencesInteractor(preferencesInteractorImpl: PreferencesInteractorImpl): PreferencesInteractor
    }
}