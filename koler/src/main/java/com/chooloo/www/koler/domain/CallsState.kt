package com.chooloo.www.koler.domain

enum class CallsState {
    NO_CALLS,
    INCOMING,
    WAITING_FOR_ACCOUNT,
    OUTGOING,
    ACTIVE,
    ACTIVE_MULTI,
    DISCONNECTED,
}

enum class ProximityScreenOff { HOLD, RELEASE_NOW, RELEASE_WHEN_UNCOVERED }

private val PRIMARY_CALL_PRIORITY = listOf(
    CallState.INCOMING,
    CallState.DIALING,
    CallState.CONNECTING,
    CallState.SELECT_PHONE_ACCOUNT,
    CallState.ACTIVE,
    CallState.DISCONNECTING,
    CallState.DISCONNECTED,
    CallState.HOLDING,
)

private val ONGOING_STATES = setOf(
    CallState.ACTIVE,
    CallState.HOLDING,
    CallState.DISCONNECTING,
    CallState.CONFERENCED,
)

private val HELD_TO_EAR_STATES = setOf(
    CallState.CONNECTING,
    CallState.DIALING,
    CallState.ACTIVE,
    CallState.HOLDING,
    CallState.CONFERENCED,
)

fun primaryCall(calls: List<Call>): Call? =
    PRIMARY_CALL_PRIORITY.firstNotNullOfOrNull { state -> calls.firstOrNull { it.state == state } }

fun callsState(calls: List<Call>, hasPendingOutgoing: Boolean): CallsState {
    fun allIn(vararg states: CallState) = calls.all { it.state in states }

    return when {
        calls.isEmpty() -> if (hasPendingOutgoing) CallsState.OUTGOING else CallsState.NO_CALLS
        calls.any { it.state == CallState.INCOMING } -> CallsState.INCOMING
        allIn(CallState.SELECT_PHONE_ACCOUNT) -> CallsState.WAITING_FOR_ACCOUNT
        allIn(CallState.CONNECTING, CallState.DIALING) -> CallsState.OUTGOING
        calls.all { it.state in ONGOING_STATES } -> if (calls.size > 1) CallsState.ACTIVE_MULTI else CallsState.ACTIVE
        allIn(CallState.DISCONNECTED) -> CallsState.DISCONNECTED
        else -> CallsState.ACTIVE
    }
}

fun proximityScreenOff(calls: List<Call>, route: AudioRoute?): ProximityScreenOff = when {
    calls.none { it.state in HELD_TO_EAR_STATES } -> ProximityScreenOff.RELEASE_WHEN_UNCOVERED
    route == AudioRoute.EARPIECE -> ProximityScreenOff.HOLD
    else -> ProximityScreenOff.RELEASE_NOW
}
