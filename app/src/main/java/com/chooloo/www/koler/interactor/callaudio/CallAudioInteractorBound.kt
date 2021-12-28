package com.chooloo.www.koler.interactor.callaudio

interface CallAudioInteractorBound : CallAudioInteractor {
    fun askForRoute(callback: (CallAudioInteractor.AudioRoute) -> Unit)
}