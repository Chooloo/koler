package com.chooloo.www.koler.di.activitycomponent

import com.chooloo.www.koler.di.contextcomponent.ContextComponentImpl
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractorBoundImpl
import com.chooloo.www.koler.interactor.dialog.DialogInteractorImpl
import com.chooloo.www.koler.interactor.navigation.NavigationInteractorImpl
import com.chooloo.www.koler.interactor.permission.PermissionInteractorImpl
import com.chooloo.www.koler.interactor.proximity.ProximityInteractorImpl
import com.chooloo.www.koler.interactor.screen.ScreenInteractorImpl
import com.chooloo.www.koler.interactor.sim.SimInteractorImpl
import com.chooloo.www.koler.ui.base.BaseActivity
import io.reactivex.disposables.CompositeDisposable

class ActivityComponentImpl(
    private val activity: BaseActivity
) : ContextComponentImpl(activity.application), ActivityComponent {
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
            dialogs,
            subscriptionManager,
            permissions
        )
    }

    override val dialogs by lazy {
        DialogInteractorImpl(activity)
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
        PermissionInteractorImpl(
            activity,
            strings,
            telecomManager
        )
    }

    override val navigations by lazy {
        NavigationInteractorImpl(
            activity,
            sims,
            telecomManager,
            strings,
            permissions,
            preferences
        )
    }

    override val callAudios by lazy {
        CallAudioInteractorBoundImpl(dialogs, strings)
    }
}