package com.chooloo.www.koler.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.domain.primaryCall
import com.chooloo.www.koler.domain.setSpeaker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var callRepository: CallRepository

    override fun onReceive(context: Context, intent: Intent) {
        val primaryCallId = primaryCall(callRepository.calls.value)?.id
        when (intent.action) {
            ACTION_ANSWER -> primaryCallId?.let(callRepository::answer)
            ACTION_DECLINE -> primaryCallId?.let(callRepository::decline)
            ACTION_MUTE_ON -> callRepository.setMuted(true)
            ACTION_MUTE_OFF -> callRepository.setMuted(false)
            ACTION_SPEAKER_ON -> callRepository.setSpeaker(true)
            ACTION_SPEAKER_OFF -> callRepository.setSpeaker(false)
        }
    }

    companion object {
        const val ACTION_ANSWER = "action_answer"
        const val ACTION_DECLINE = "action_decline"
        const val ACTION_MUTE_ON = "action_mute_on"
        const val ACTION_MUTE_OFF = "action_mute_off"
        const val ACTION_SPEAKER_ON = "action_speaker_on"
        const val ACTION_SPEAKER_OFF = "action_speaker_off"
    }
}
