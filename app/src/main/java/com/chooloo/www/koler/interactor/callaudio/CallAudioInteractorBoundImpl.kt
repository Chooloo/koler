package com.chooloo.www.koler.interactor.callaudio

import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.dialog.DialogInteractor
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class CallAudioInteractorBoundImpl(
    private val dialogInteractor: DialogInteractor,
    private val stringInteractor: StringInteractor,
    private val callAudioInteractor: CallAudioInteractor
) : BaseObservable<CallAudioInteractorBound.Listener>(), CallAudioInteractorBound {
    override fun askForRoute(callback: (CallAudioInteractor.AudioRoute) -> Unit) {
        val audioRoutes = callAudioInteractor.supportedAudioRoutes
        val strings = audioRoutes.map { stringInteractor.getString(it.stringRes) }.toTypedArray()
        dialogInteractor.askForChoice(
            strings,
            R.drawable.ic_volume_up_black_24dp,
            R.string.action_choose_audio_route,
            { _, index -> callback.invoke(audioRoutes[index]) }
        )
    }
}