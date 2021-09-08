package com.chooloo.www.koler.di.boundcomponent

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.di.component.ComponentRoot
import com.chooloo.www.koler.di.factory.livedata.LiveDataFactory
import com.chooloo.www.koler.interactor.animation.AnimationInteractor
import com.chooloo.www.koler.interactor.audio.AudioInteractor
import com.chooloo.www.koler.interactor.calls.CallsInteractor
import com.chooloo.www.koler.interactor.color.ColorInteractor
import com.chooloo.www.koler.interactor.contacts.ContactsInteractor
import com.chooloo.www.koler.interactor.dialog.DialogInteractorImpl
import com.chooloo.www.koler.interactor.drawable.DrawableInteractor
import com.chooloo.www.koler.interactor.navigation.NavigationInteractorImpl
import com.chooloo.www.koler.interactor.numbers.NumbersInteractor
import com.chooloo.www.koler.interactor.permission.PermissionInteractorImpl
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractor
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor
import com.chooloo.www.koler.interactor.proximity.ProximityInteractorImpl
import com.chooloo.www.koler.interactor.recents.RecentsInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractorImpl
import com.chooloo.www.koler.interactor.sim.SimInteractorImpl
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.PreferencesManager
import io.reactivex.disposables.CompositeDisposable

class BoundComponentRootImpl(
    private val activity: BaseActivity
) : BoundComponentRoot, ComponentRoot {
    private val componentRoot get() = (activity.applicationContext as KolerApp).componentRoot

    override val lifecycleOwner by lazy {
        activity
    }
    
    override val disposables by lazy {
        CompositeDisposable()
    }


    override val simInteractor by lazy {
        SimInteractorImpl(
            activity,
            componentRoot.telecomManager,
            dialogInteractor,
            componentRoot.subscriptionManager,
            permissionInteractor
        )
    }

    override val dialogInteractor by lazy {
        DialogInteractorImpl(activity)
    }

    override val screenInteractor by lazy {
        ScreenInteractorImpl(
            activity,
            componentRoot.keyguardManager,
            componentRoot.inputMethodManager
        )
    }

    override val proximityInteractor by lazy {
        ProximityInteractorImpl(activity, componentRoot.powerManager)
    }

    override val permissionInteractor by lazy {
        PermissionInteractorImpl(
            activity,
            componentRoot.telecomManager,
            componentRoot.stringInteractor
        )
    }

    override val navigationInteractor by lazy {
        NavigationInteractorImpl(
            activity,
            simInteractor,
            componentRoot.telecomManager,
            componentRoot.stringInteractor,
            permissionInteractor
        )
    }


    override val liveDataFactory: LiveDataFactory
        get() = componentRoot.liveDataFactory

    override val vibrator: Vibrator
        get() = componentRoot.vibrator

    override val powerManager: PowerManager
        get() = componentRoot.powerManager

    override val audioManager: AudioManager
        get() = componentRoot.audioManager

    override val telecomManager: TelecomManager
        get() = componentRoot.telecomManager

    override val keyguardManager: KeyguardManager
        get() = componentRoot.keyguardManager

    override val clipboardManager: ClipboardManager
        get() = componentRoot.clipboardManager

    override val inputMethodManager: InputMethodManager
        get() = componentRoot.inputMethodManager

    override val preferencesManager: PreferencesManager
        get() = componentRoot.preferencesManager

    override val subscriptionManager: SubscriptionManager
        get() = componentRoot.subscriptionManager

    override val notificationManager: NotificationManagerCompat
        get() = componentRoot.notificationManager


    override val colorInteractor: ColorInteractor
        get() = componentRoot.colorInteractor

    override val audioInteractor: AudioInteractor
        get() = componentRoot.audioInteractor

    override val callsInteractor: CallsInteractor
        get() = componentRoot.callsInteractor

    override val stringInteractor: StringInteractor
        get() = componentRoot.stringInteractor

    override val numbersInteractor: NumbersInteractor
        get() = componentRoot.numbersInteractor

    override val recentsInteractor: RecentsInteractor
        get() = componentRoot.recentsInteractor

    override val drawableInteractor: DrawableInteractor
        get() = componentRoot.drawableInteractor

    override val contactsInteractor: ContactsInteractor
        get() = componentRoot.contactsInteractor

    override val animationInteractor: AnimationInteractor
        get() = componentRoot.animationInteractor

    override val preferencesInteractor: PreferencesInteractor
        get() = componentRoot.preferencesInteractor

    override val phoneAccountsInteractor: PhoneAccountsInteractor
        get() = componentRoot.phoneAccountsInteractor

}