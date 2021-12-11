package com.chooloo.www.koler.di.contextcomponent

import android.app.Application
import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.koler.di.factory.livedata.LiveDataFactoryImpl
import com.chooloo.www.koler.interactor.animation.AnimationInteractorImpl
import com.chooloo.www.koler.interactor.audio.AudioInteractorImpl
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractorImpl
import com.chooloo.www.koler.interactor.calls.CallsInteractorImpl
import com.chooloo.www.koler.interactor.color.ColorInteractorImpl
import com.chooloo.www.koler.interactor.contacts.ContactsInteractorImpl
import com.chooloo.www.koler.interactor.drawable.DrawableInteractorImpl
import com.chooloo.www.koler.interactor.numbers.NumbersInteractorImpl
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractorImpl
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.koler.interactor.recents.RecentsInteractorImpl
import com.chooloo.www.koler.interactor.string.StringInteractorImpl
import com.chooloo.www.koler.util.PreferencesManager

open class ContextComponentImpl(
    internal val application: Application
) : ContextComponent {
    override val liveDataFactory by lazy {
        LiveDataFactoryImpl(application)
    }

    override val vibrator by lazy {
        application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override val powerManager by lazy {
        application.getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    override val audioManager by lazy {
        application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override val telecomManager by lazy {
        application.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    }

    override val keyguardManager by lazy {
        application.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }

    override val clipboardManager by lazy {
        application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override val inputMethodManager by lazy {
        application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override val preferencesManager by lazy {
        PreferencesManager.getInstance(application)
    }

    override val subscriptionManager by lazy {
        application.getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    }

    override val notificationManager by lazy {
        NotificationManagerCompat.from(application)
    }


    override val colorInteractor by lazy {
        ColorInteractorImpl(application)
    }

    override val audioInteractor by lazy {
        AudioInteractorImpl(vibrator, audioManager)
    }

    override val callsInteractor by lazy {
        CallsInteractorImpl()
    }

    override val stringInteractor by lazy {
        StringInteractorImpl(application)
    }

    override val numbersInteractor by lazy {
        NumbersInteractorImpl(application, telecomManager)
    }

    override val recentsInteractor by lazy {
        RecentsInteractorImpl(application)
    }

    override val drawableInteractor by lazy {
        DrawableInteractorImpl(application)
    }

    override val contactsInteractor by lazy {
        ContactsInteractorImpl(application, numbersInteractor, phoneAccountsInteractor)
    }

    override val animationInteractor by lazy {
        AnimationInteractorImpl(preferencesInteractor)
    }

    override val callAudioInteractor by lazy {
        CallAudioInteractorImpl()
    }

    override val preferencesInteractor by lazy {
        PreferencesInteractorImpl(preferencesManager)
    }

    override val phoneAccountsInteractor by lazy {
        PhoneAccountsInteractorImpl(application)
    }
}