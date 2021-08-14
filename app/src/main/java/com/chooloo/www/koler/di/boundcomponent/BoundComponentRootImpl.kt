package com.chooloo.www.koler.di.boundcomponent

import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.interactor.proximity.ProximityInteractorImpl
import com.chooloo.www.koler.interactor.screen.ScreenInteractorImpl
import com.chooloo.www.koler.ui.base.BaseActivity

class BoundComponentRootImpl(
    private val activity: BaseActivity
) : BoundComponentRoot {

    private val componentRoot by lazy {
        (activity.applicationContext as KolerApp).componentRoot
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
}