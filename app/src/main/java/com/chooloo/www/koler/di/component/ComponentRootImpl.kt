package com.chooloo.www.koler.di.component

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Vibrator
import android.telecom.TelecomManager
import com.chooloo.www.koler.interactor.animation.AnimationInteractorImpl
import com.chooloo.www.koler.interactor.audio.AudioInteractorImpl
import com.chooloo.www.koler.interactor.contacts.ContactsInteractorImpl
import com.chooloo.www.koler.interactor.numbers.NumbersInteractorImpl
import com.chooloo.www.koler.interactor.permission.PermissionInteractorImpl
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractorImpl
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.koler.util.PreferencesManager

class ComponentRootImpl(
    internal val application: Application
) : ComponentRoot {
    override val vibrator by lazy {
        application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override val audioManager by lazy {
        application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override val telecomManager by lazy {
        application.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    }

    override val preferencesManager by lazy {
        PreferencesManager.getInstance(application)
    }


    override val audioInteractor by lazy {
        AudioInteractorImpl(vibrator, audioManager)
    }

    override val numbersInteractor by lazy {
        NumbersInteractorImpl(application)
    }

    override val contactsInteractor by lazy {
        ContactsInteractorImpl(
            application,
            numbersInteractor,
            permissionInteractor,
            preferencesInteractor,
            phoneAccountsInteractor
        )
    }

    override val animationInteractor by lazy {
        AnimationInteractorImpl(preferencesInteractor)
    }
    override val permissionInteractor by lazy {
        PermissionInteractorImpl(application, telecomManager)
    }

    override val preferencesInteractor by lazy {
        PreferencesInteractorImpl(preferencesManager)
    }

    override val phoneAccountsInteractor by lazy {
        PhoneAccountsInteractorImpl(application)
    }
}