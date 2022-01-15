package com.chooloo.www.chooloolib.di.contextcomponent

import android.app.Application
import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.media.AudioManager
import android.os.Build
import android.os.PowerManager
import android.os.Vibrator
import android.os.VibratorManager
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.BaseApp
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactoryImpl
import com.chooloo.www.chooloolib.interactor.animation.AnimationInteractorImpl
import com.chooloo.www.chooloolib.interactor.audio.AudioInteractorImpl
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractorImpl
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractorImpl
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractorImpl
import com.chooloo.www.chooloolib.interactor.color.ColorInteractorImpl
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractorImpl
import com.chooloo.www.chooloolib.interactor.drawable.DrawableInteractorImpl
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractorImpl
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractorImpl
import com.chooloo.www.chooloolib.interactor.string.StringInteractorImpl
import com.chooloo.www.chooloolib.util.PreferencesManager

open class ContextComponentImpl(
    internal val application: BaseApp
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


    override val colors by lazy {
        ColorInteractorImpl(application)
    }

    override val audios by lazy {
        AudioInteractorImpl(vibrator, audioManager)
    }

    override val calls by lazy {
        CallsInteractorImpl()
    }

    override val strings by lazy {
        StringInteractorImpl(application)
    }

    override val blocked by lazy {
        BlockedInteractorImpl(application)
    }

    override val recents by lazy {
        RecentsInteractorImpl(application)
    }

    override val drawables by lazy {
        DrawableInteractorImpl(application)
    }

    override val contacts by lazy {
        ContactsInteractorImpl(application, blocked, phones)
    }

    override val animations by lazy {
        AnimationInteractorImpl(preferences)
    }

    override val callAudios by lazy {
        CallAudioInteractorImpl()
    }

    override val preferences by lazy {
        PreferencesInteractorImpl(preferencesManager)
    }

    override val phones by lazy {
        PhonesInteractorImpl(application)
    }
}