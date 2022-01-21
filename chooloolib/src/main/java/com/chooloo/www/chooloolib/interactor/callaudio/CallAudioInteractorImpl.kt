package com.chooloo.www.chooloolib.interactor.callaudio

import android.telecom.CallAudioState
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractor.AudioRoute
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractor.AudioRoute.*
import com.chooloo.www.chooloolib.service.CallService
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable

open class CallAudioInteractorImpl :
    BaseObservable<CallAudioInteractor.Listener>(),
    CallAudioInteractor {

    private val callAudioState get() = CallService.sInstance?.callAudioState


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

    override val supportedAudioRoutes: Array<AudioRoute>
        get() = values().filter {
            callAudioState?.supportedRouteMask?.let(it.route::and) == it.route
        }.toTypedArray()


    override fun entryCallAudioStateChanged(audioState: CallAudioState) {
        invokeListeners { l ->
            audioState.isMuted.let { l.onMuteChanged(it) }
            AudioRoute.fromRoute(audioState.route)?.let { l.onAudioRouteChanged(it) }
        }
    }
}