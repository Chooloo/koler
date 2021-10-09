package com.chooloo.www.koler.interactor.callaudio

import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor.AudioRoute
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor.AudioRoute.*
import com.chooloo.www.koler.service.CallService
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class CallAudioInteractorImpl :
    BaseObservable<CallAudioInteractor.Listener>(),
    CallAudioInteractor {

    private val callAudioState get() = CallService.sInstance?.callAudioState


    override val supportedAudioRoutes: Array<AudioRoute>
        get() = AudioRoute.values().filter {
            callAudioState?.supportedRouteMask?.let(it.route::and) == it.route
        }.toTypedArray()


    override var isMuted: Boolean?
        get() = callAudioState?.isMuted
        set(value) {
            value?.let { CallService.sInstance?.setMuted(it) }
        }

    override var isSpeakerOn: Boolean?
        get() = audioRoute == SPEAKER
        set(value) {
            if (value == true) {
                audioRoute = SPEAKER
            } else if (value == false && audioRoute != BLUETOOTH) {
                audioRoute = WIRED_OR_EARPIECE
            }
        }

    override var audioRoute: AudioRoute?
        get() = Companion.fromRoute(callAudioState?.route)
        set(value) {
            value?.route?.let { CallService.sInstance?.setAudioRoute(it) }
        }
}