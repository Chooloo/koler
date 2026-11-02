package com.chooloo.www.koler.domain

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
    fun setMuted(isMuted: Boolean)
    fun setAudioRoute(route: AudioRoute)
}

fun CallRepository.setSpeaker(isOn: Boolean) {
    when {
        isOn -> setAudioRoute(AudioRoute.SPEAKER)
        audio.value.route != AudioRoute.BLUETOOTH -> setAudioRoute(AudioRoute.WIRED_OR_EARPIECE)
    }
}
