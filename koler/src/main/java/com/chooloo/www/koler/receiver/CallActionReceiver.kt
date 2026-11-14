package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chooloo.www.koler.AppConstants
import com.chooloo.www.koler.domain.repository.CallRepository
import com.chooloo.www.koler.domain.usecase.GetPrimaryCall
import com.chooloo.www.koler.domain.usecase.SetSpeaker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallActionReceiver : BroadcastReceiver() {
    @Inject lateinit var callRepository: CallRepository

    @Inject lateinit var getPrimaryCall: GetPrimaryCall

    @Inject lateinit var setSpeaker: SetSpeaker

    override fun onReceive(context: Context, intent: Intent) {
        val primaryCallId = getPrimaryCall(callRepository.calls.value)?.id
        when (intent.action) {
            AppConstants.ACTION_ANSWER -> primaryCallId?.let(callRepository::answer)
            AppConstants.ACTION_DECLINE -> primaryCallId?.let(callRepository::decline)
            AppConstants.ACTION_MUTE_ON -> callRepository.setMuted(true)
            AppConstants.ACTION_MUTE_OFF -> callRepository.setMuted(false)
            AppConstants.ACTION_SPEAKER_ON -> setSpeaker(true)
            AppConstants.ACTION_SPEAKER_OFF -> setSpeaker(false)
        }
    }
}
