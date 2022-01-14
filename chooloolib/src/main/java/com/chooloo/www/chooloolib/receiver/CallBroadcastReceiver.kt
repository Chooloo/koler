package com.chooloo.www.chooloolib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.chooloolib.BaseApp

class CallBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val component = (context.applicationContext as BaseApp).component
        when (intent.action) {
            ACTION_MUTE -> component.audios.isMuted = true
            ACTION_UNMUTE -> component.audios.isMuted = false
            ACTION_ANSWER -> component.calls.mainCall?.answer()
            ACTION_HANGUP -> component.calls.mainCall?.reject()
            ACTION_SPEAKER -> component.audios.isSpeakerOn = true
            ACTION_UNSPEAKER -> component.audios.isSpeakerOn = false
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