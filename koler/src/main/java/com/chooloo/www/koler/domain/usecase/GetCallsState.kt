package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.enum.CallsState
import com.chooloo.www.koler.domain.model.Call
import javax.inject.Inject

class GetCallsState @Inject constructor() {
    operator fun invoke(calls: List<Call>, hasPendingOutgoing: Boolean): CallsState {
        fun allIn(vararg states: CallState) = calls.all { it.state in states }

        return when {
            calls.isEmpty() -> if (hasPendingOutgoing) CallsState.OUTGOING else CallsState.NO_CALLS

            calls.any { it.state == CallState.INCOMING } -> CallsState.INCOMING

            allIn(CallState.SELECT_PHONE_ACCOUNT) -> CallsState.WAITING_FOR_ACCOUNT

            allIn(CallState.CONNECTING, CallState.DIALING) -> CallsState.OUTGOING

            calls.all { it.state in ONGOING_STATES } ->
                if (calls.size > 1) CallsState.ACTIVE_MULTI else CallsState.ACTIVE

            allIn(CallState.DISCONNECTED) -> CallsState.DISCONNECTED

            else -> CallsState.ACTIVE
        }
    }

    private companion object {
        val ONGOING_STATES = setOf(
            CallState.ACTIVE,
            CallState.HOLDING,
            CallState.DISCONNECTING,
            CallState.CONFERENCED,
        )
    }
}
