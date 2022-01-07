package com.chooloo.www.koler.di.activitycomponent

import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.interactor.dialog.DialogsInteractorImpl
import com.chooloo.www.koler.interactor.navigation.NavigationInteractorImpl
import com.chooloo.www.koler.interactor.permission.PermissionsInteractorImpl
import com.chooloo.www.koler.interactor.proximity.ProximityInteractorImpl
import com.chooloo.www.koler.interactor.screen.ScreenInteractorImpl
import com.chooloo.www.koler.interactor.sim.SimInteractorImpl
import com.chooloo.www.koler.ui.base.BaseActivity
import io.reactivex.disposables.CompositeDisposable

class ActivityComponentImpl(
    private val activity: BaseActivity
) : ActivityComponent {
    private val contextComponent get() = (activity.applicationContext as KolerApp).component

    override val disposables by lazy {
        CompositeDisposable()
    }


    override val lifecycleOwner by lazy {
        activity
    }

    override val viewModelStoreOwner by lazy {
        activity
    }


    override val sims by lazy {
        SimInteractorImpl(
            activity,
            telecomManager,
            subscriptionManager,
            permissions
        )
    }

    override val dialogs by lazy {
        DialogsInteractorImpl(activity)
    }

    override val screens by lazy {
        ScreenInteractorImpl(
            activity,
            keyguardManager,
            inputMethodManager
        )
    }

    override val proximities by lazy {
        ProximityInteractorImpl(activity, powerManager)
    }

    override val permissions by lazy {
        PermissionsInteractorImpl(
            activity,
            strings,
            telecomManager
        )
    }

    override val navigations by lazy {
        NavigationInteractorImpl(
            sims,
            activity,
            dialogs,
            strings,
            telecomManager,
            permissions,
            preferences
        )
    }


    override val liveDataFactory get() = contextComponent.liveDataFactory

    override val vibrator get() = contextComponent.vibrator
    override val powerManager get() = contextComponent.powerManager
    override val audioManager get() = contextComponent.audioManager
    override val telecomManager get() = contextComponent.telecomManager
    override val keyguardManager get() = contextComponent.keyguardManager
    override val clipboardManager get() = contextComponent.clipboardManager
    override val inputMethodManager get() = contextComponent.inputMethodManager
    override val preferencesManager get() = contextComponent.preferencesManager
    override val subscriptionManager get() = contextComponent.subscriptionManager
    override val notificationManager get() = contextComponent.notificationManager

    override val calls get() = contextComponent.calls
    override val colors get() = contextComponent.colors
    override val audios get() = contextComponent.audios
    override val phones get() = contextComponent.phones
    override val strings get() = contextComponent.strings
    override val blocked get() = contextComponent.blocked
    override val recents get() = contextComponent.recents
    override val drawables get() = contextComponent.drawables
    override val contacts get() = contextComponent.contacts
    override val callAudios get() = contextComponent.callAudios
    override val animations get() = contextComponent.animations
    override val preferences get() = contextComponent.preferences
}