package com.chooloo.www.koler.ui.call

import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.domain.repository.CallRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCallRepository : CallRepository {
    override val calls = MutableStateFlow<List<Call>>(emptyList())
    override val audio = MutableStateFlow(CallAudio())
    override val canAddCall = MutableStateFlow(true)
    override val hasPendingOutgoing = MutableStateFlow(false)

    val actions = mutableListOf<String>()

    override fun answer(callId: String) = record("answer $callId")
    override fun decline(callId: String) = record("decline $callId")
    override fun hold(callId: String) = record("hold $callId")
    override fun unhold(callId: String) = record("unhold $callId")
    override fun swap(callId: String) = record("swap $callId")
    override fun merge(callId: String) = record("merge $callId")
    override fun separate(callId: String) = record("separate $callId")
    override fun playDtmf(callId: String, key: Char) = record("dtmf $callId $key")
    override fun selectPhoneAccount(callId: String, accountId: String) =
        record("account $callId $accountId")
    override fun setMuted(isMuted: Boolean) = record("muted $isMuted")
    override fun setAudioRoute(route: AudioRoute) = record("route $route")

    private fun record(action: String) {
        actions += action
    }
}
