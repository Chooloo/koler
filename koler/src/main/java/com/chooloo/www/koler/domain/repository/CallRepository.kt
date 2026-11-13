package com.chooloo.www.koler.domain.repository

import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import kotlinx.coroutines.flow.StateFlow

interface CallRepository {
    val calls: StateFlow<List<Call>>
    val audio: StateFlow<CallAudio>
    val canAddCall: StateFlow<Boolean>
    val hasPendingOutgoing: StateFlow<Boolean>

    fun answer(callId: String)
    fun decline(callId: String)
    fun hold(callId: String)
    fun unhold(callId: String)
    fun swap(callId: String)
    fun merge(callId: String)
    fun separate(callId: String)
    fun playDtmf(callId: String, key: Char)
    fun selectPhoneAccount(callId: String, accountId: String)
    fun setMuted(isMuted: Boolean)
    fun setAudioRoute(route: AudioRoute)
}
