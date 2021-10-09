package com.chooloo.www.koler.interactor.callaudio

import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor.AudioRoute
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor.AudioRoute.*
import com.chooloo.www.koler.interactor.dialog.DialogInteractor
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.service.CallService
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class CallAudioInteractorImpl(
    private val dialogInteractor: DialogInteractor,
    private val stringInteractor: StringInteractor
) :
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


    override fun askForRoute(callback: (AudioRoute) -> Unit) {
        val audioRoutes = supportedAudioRoutes
        val strings = audioRoutes.map { stringInteractor.getString(it.stringRes) }.toTypedArray()
        dialogInteractor.askForChoice(
            strings,
            R.drawable.ic_volume_up_black_24dp,
            R.string.action_choose_audio_route,
            { _, index -> callback.invoke(audioRoutes[index]) }
        )
    }
}