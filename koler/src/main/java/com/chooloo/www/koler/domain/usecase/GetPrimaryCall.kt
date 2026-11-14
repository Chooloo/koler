package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import javax.inject.Inject

class GetPrimaryCall @Inject constructor() {
    operator fun invoke(calls: List<Call>): Call? =
        PRIORITY.firstNotNullOfOrNull { state -> calls.firstOrNull { it.state == state } }

    private companion object {
        val PRIORITY = listOf(
            CallState.INCOMING,
            CallState.DIALING,
            CallState.CONNECTING,
            CallState.SELECT_PHONE_ACCOUNT,
            CallState.ACTIVE,
            CallState.DISCONNECTING,
            CallState.DISCONNECTED,
            CallState.HOLDING,
        )
    }
}
