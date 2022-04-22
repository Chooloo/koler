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
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactoryImpl
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactoryImpl
import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactoryImpl
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
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractorImpl
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractorImpl
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractorImpl
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractorImpl
import com.chooloo.www.chooloolib.interactor.rawcontacts.RawContactsInteractor
import com.chooloo.www.chooloolib.interactor.rawcontacts.RawContactsInteractorImpl
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractorImpl
import com.chooloo.www.chooloolib.interactor.sim.SimsInteractor
import com.chooloo.www.chooloolib.interactor.sim.SimsInteractorImpl
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractorImpl
import com.chooloo.www.chooloolib.repository.calls.CallsRepository
import com.chooloo.www.chooloolib.repository.calls.CallsRepositoryImpl
import com.chooloo.www.chooloolib.repository.contacts.ContactsRepository
import com.chooloo.www.chooloolib.repository.contacts.ContactsRepositoryImpl
import com.chooloo.www.chooloolib.repository.phones.PhonesRepository
import com.chooloo.www.chooloolib.repository.phones.PhonesRepositoryImpl
import com.chooloo.www.chooloolib.repository.rawcontacts.RawContactsRepository
import com.chooloo.www.chooloolib.repository.rawcontacts.RawContactsRepositoryImpl
import com.chooloo.www.chooloolib.repository.recents.RecentsRepository
import com.chooloo.www.chooloolib.repository.recents.RecentsRepositoryImpl
import com.chooloo.www.chooloolib.util.PreferencesManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable

@InstallIn(SingletonComponent::class)
@Module(includes = [ApplicationModule.BindsModule::class])
class ApplicationModule {
    @Provides
    fun provideDisposables(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    //region manager

    @Provides
    fun providePowerManager(@ApplicationContext context: Context): PowerManager =
        context.getSystemService(Context.POWER_SERVICE) as PowerManager

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    fun provideTelecomManager(@ApplicationContext context: Context): TelecomManager =
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    @Provides
    fun provideKeyguardManager(@ApplicationContext context: Context): KeyguardManager =
        context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    fun provideInputMethodManager(@ApplicationContext context: Context): InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager =
        PreferencesManager.getInstance(context)

    @Provides
    fun provideSubscriptionManager(@ApplicationContext context: Context): SubscriptionManager =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    //endregion

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        //region repository

        @Binds
        fun bindCallsRepository(callsRepositoryImpl: CallsRepositoryImpl): CallsRepository

        @Binds
        fun bindPhonesRepository(phonesRepositoryImpl: PhonesRepositoryImpl): PhonesRepository

        @Binds
        fun bindRecentsRepository(recentsRepositoryImpl: RecentsRepositoryImpl): RecentsRepository

        @Binds
        fun bindContactsRepository(contactsRepositoryImpl: ContactsRepositoryImpl): ContactsRepository

        @Binds
        fun bindRawContactsRepoistory(rawContactsRepositoryImpl: RawContactsRepositoryImpl): RawContactsRepository

        //endregion

        //region factory

        @Binds
        fun bindLiveDataFactory(liveDataFactoryImpl: LiveDataFactoryImpl): LiveDataFactory

        @Binds
        fun bindFragmentFactory(fragmentFactoryImpl: FragmentFactoryImpl): FragmentFactory

        @Binds
        fun bindContentResolverFactory(contentResolverFactoryImpl: ContentResolverFactoryImpl): ContentResolverFactory

        //endregion

        //region interactor

        @Binds
        fun bindSimsInteractor(simsInteractorImpl: SimsInteractorImpl): SimsInteractor

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
        fun bindRawContactsInteractor(rawContactsInteractorImpl: RawContactsInteractorImpl): RawContactsInteractor

        @Binds
        fun bindProximitiesInteractor(proximitiesInteractorImpl: ProximitiesInteractorImpl): ProximitiesInteractor

        @Binds
        fun bindPermissionsInteractor(permissionsInteractorImpl: PermissionsInteractorImpl): PermissionsInteractor

        @Binds
        fun bindPreferencesInteractor(preferencesInteractorImpl: PreferencesInteractorImpl): PreferencesInteractor

        @Binds
        fun bindNavigationsInteractor(navigationsInteractorImpl: NavigationsInteractorImpl): NavigationsInteractor

        //endregion
    }
}