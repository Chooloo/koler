package com.chooloo.www.chooloolib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var callsInteractor: CallsInteractor
    @Inject lateinit var audiosInteractor: AudiosInteractor

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_MUTE -> audiosInteractor.isMuted = true
            ACTION_UNMUTE -> audiosInteractor.isMuted = false
            ACTION_ANSWER -> callsInteractor.mainCall?.answer()
            ACTION_HANGUP -> callsInteractor.mainCall?.reject()
            ACTION_SPEAKER -> audiosInteractor.isSpeakerOn = true
            ACTION_UNSPEAKER -> audiosInteractor.isSpeakerOn = false
        }
    }

    companion object {
        const val ACTION_MUTE = "action_mute"
        const val ACTION_UNMUTE = "action_unmute"
        const val ACTION_ANSWER = "action_answer"
        const val ACTION_HANGUP = "action_hangup"
        const val ACTION_SPEAKER = "action_speaker"
        const val ACTION_UNSPEAKER = "action_unspeaker"
    }
}