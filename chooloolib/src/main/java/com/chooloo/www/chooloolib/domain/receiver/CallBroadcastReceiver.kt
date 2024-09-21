package com.chooloo.www.chooloolib.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var callRepository: CallRepository
    @Inject @IoScope lateinit var ioScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        ioScope.launch {
            when (intent.action) {
                ACTION_ANSWER -> callRepository.answerMainCall()
                ACTION_DECLINE -> callRepository.declineMainCall()
                ACTION_MUTE_ON -> callRepository.toggleMute(true)
                ACTION_MUTE_OFF -> callRepository.toggleMute(false)
                ACTION_SPEAKER_ON -> callRepository.toggleSpeaker(true)
                ACTION_SPEAKER_OFF -> callRepository.toggleSpeaker(false)
            }
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