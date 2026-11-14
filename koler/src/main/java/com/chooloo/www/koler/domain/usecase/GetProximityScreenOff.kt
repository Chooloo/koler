package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.enum.ProximityScreenOff
import com.chooloo.www.koler.domain.model.Call
import javax.inject.Inject

class GetProximityScreenOff @Inject constructor() {
    operator fun invoke(calls: List<Call>, route: AudioRoute?): ProximityScreenOff = when {
        calls.none { it.state in HELD_TO_EAR_STATES } -> ProximityScreenOff.RELEASE_WHEN_UNCOVERED
        route == AudioRoute.EARPIECE -> ProximityScreenOff.HOLD
        else -> ProximityScreenOff.RELEASE_NOW
    }

    private companion object {
        val HELD_TO_EAR_STATES = setOf(
            CallState.CONNECTING,
            CallState.DIALING,
            CallState.ACTIVE,
            CallState.HOLDING,
            CallState.CONFERENCED,
        )
    }
}
