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


    override val simInteractor by lazy {
        SimInteractorImpl(
            activity,
            telecomManager,
            dialogInteractor,
            subscriptionManager,
            permissionInteractor
        )
    }

    override val dialogInteractor by lazy {
        DialogInteractorImpl(activity)
    }

    override val screenInteractor by lazy {
        ScreenInteractorImpl(
            activity,
            keyguardManager,
            inputMethodManager
        )
    }

    override val proximityInteractor by lazy {
        ProximityInteractorImpl(activity, powerManager)
    }

    override val permissionInteractor by lazy {
        PermissionInteractorImpl(
            activity,
            telecomManager,
            stringInteractor
        )
    }

    override val navigationInteractor by lazy {
        NavigationInteractorImpl(
            activity,
            simInteractor,
            telecomManager,
            stringInteractor,
            permissionInteractor,
            preferencesInteractor
        )
    }

    override val callAudioInteractorBound by lazy {
        CallAudioInteractorBoundImpl(dialogInteractor, stringInteractor, callAudioInteractor)
    }
}