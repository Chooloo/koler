package com.chooloo.www.koler.di.boundcomponent

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.koler.di.component.ComponentRoot
import com.chooloo.www.koler.interactor.proximity.ProximityInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractor
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.livedata.PhoneProviderLiveData
import com.chooloo.www.koler.livedata.RecentsProviderLiveData

interface BoundComponentRoot : ComponentRoot {
    val lifecycleOwner: LifecycleOwner

    val screenInteractor: ScreenInteractor
    val proximityInteractor: ProximityInteractor

    fun addObserver(lifecycleObserver: LifecycleObserver)
}