package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.KolerApp

class CallBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val componentRoot = (context.applicationContext as KolerApp).component
        when (intent.action) {
            ACTION_MUTE -> componentRoot.audios.isMuted = true
            ACTION_UNMUTE -> componentRoot.audios.isMuted = false
            ACTION_ANSWER -> componentRoot.calls.mainCall?.answer()
            ACTION_HANGUP -> componentRoot.calls.mainCall?.reject()
            ACTION_SPEAKER -> componentRoot.audios.isSpeakerOn = true
            ACTION_UNSPEAKER -> componentRoot.audios.isSpeakerOn = false
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