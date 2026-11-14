package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.enum.ProximityScreenOff
import com.chooloo.www.koler.domain.model.Call
import org.junit.Assert.assertEquals
import org.junit.Test

class GetProximityScreenOffTest {
    private val getProximityScreenOff = GetProximityScreenOff()

    private fun call(id: String, state: CallState) = Call(id = id, state = state, number = id)

    @Test
    fun `proximity lock is held on the earpiece while a call is at the ear`() {
        listOf(
            CallState.CONNECTING,
            CallState.DIALING,
            CallState.ACTIVE,
            CallState.HOLDING,
            CallState.CONFERENCED,
        ).forEach { state ->
            assertEquals(
                ProximityScreenOff.HOLD,
                getProximityScreenOff(listOf(call("a", state)), AudioRoute.EARPIECE),
            )
        }
    }

    @Test
    fun `proximity lock is released at once on any other route`() {
        val calls = listOf(call("a", CallState.ACTIVE))

        listOf(
            AudioRoute.SPEAKER,
            AudioRoute.BLUETOOTH,
            AudioRoute.WIRED_HEADSET,
            null,
        ).forEach { route ->
            assertEquals(ProximityScreenOff.RELEASE_NOW, getProximityScreenOff(calls, route))
        }
    }

    @Test
    fun `proximity lock waits for uncovering when no call is at the ear`() {
        listOf(
            emptyList(),
            listOf(call("a", CallState.INCOMING)),
            listOf(call("a", CallState.SELECT_PHONE_ACCOUNT)),
            listOf(call("a", CallState.DISCONNECTING)),
            listOf(call("a", CallState.DISCONNECTED)),
        ).forEach { calls ->
            assertEquals(
                ProximityScreenOff.RELEASE_WHEN_UNCOVERED,
                getProximityScreenOff(calls, AudioRoute.EARPIECE),
            )
        }
    }

    @Test
    fun `proximity lock stays held when another call rings during a call`() {
        val calls = listOf(call("a", CallState.ACTIVE), call("b", CallState.INCOMING))

        assertEquals(ProximityScreenOff.HOLD, getProximityScreenOff(calls, AudioRoute.EARPIECE))
    }

    @Test
    fun `proximity lock release ignores a route reset once calls are gone`() {
        assertEquals(
            ProximityScreenOff.RELEASE_WHEN_UNCOVERED,
            getProximityScreenOff(emptyList(), null),
        )
    }
}
