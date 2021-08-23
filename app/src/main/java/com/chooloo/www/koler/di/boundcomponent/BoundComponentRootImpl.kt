package com.chooloo.www.koler.di.boundcomponent

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.di.component.ComponentRootImpl
import com.chooloo.www.koler.interactor.proximity.ProximityInteractorImpl
import com.chooloo.www.koler.interactor.screen.ScreenInteractorImpl
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.livedata.PhoneProviderLiveData
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.base.BaseActivity

class BoundComponentRootImpl(
    private val activity: BaseActivity
) : ComponentRootImpl(activity.applicationContext as KolerApp), BoundComponentRoot {
    override val lifecycleOwner by lazy {
        activity
    }


    override val screenInteractor by lazy {
        ScreenInteractorImpl(activity, keyguardManager, inputMethodManager)
    }

    override val proximityInteractor by lazy {
        ProximityInteractorImpl(activity, powerManager)
    }


    override fun addObserver(lifecycleObserver: LifecycleObserver) {
        activity.lifecycle.addObserver(lifecycleObserver)
    }
}