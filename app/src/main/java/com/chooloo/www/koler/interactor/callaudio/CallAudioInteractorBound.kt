package com.chooloo.www.koler.interactor.callaudio

import com.chooloo.www.koler.util.baseobservable.BaseObservable
import com.chooloo.www.koler.util.baseobservable.IBaseObservable

interface CallAudioInteractorBound : IBaseObservable<CallAudioInteractorBound.Listener> {
    interface Listener

    fun askForRoute(callback: (CallAudioInteractor.AudioRoute) -> Unit)
}