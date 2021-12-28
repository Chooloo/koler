package com.chooloo.www.koler.interactor.callaudio

import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.dialog.DialogInteractor
import com.chooloo.www.koler.interactor.string.StringInteractor

class CallAudioInteractorBoundImpl(
    private val dialogs: DialogInteractor,
    private val strings: StringInteractor
) : CallAudioInteractorImpl(), CallAudioInteractorBound {
    override fun askForRoute(callback: (CallAudioInteractor.AudioRoute) -> Unit) {
        val audioRoutes = supportedAudioRoutes
        dialogs.askForChoice(
            audioRoutes.map { strings.getString(it.stringRes) },
            R.drawable.ic_volume_up_black_24dp,
            R.string.action_choose_audio_route,
            { _, index -> callback.invoke(audioRoutes[index]) }
        )
    }
}