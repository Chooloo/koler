package com.chooloo.www.koler.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CallsStateTest {
    private fun call(id: String, state: CallState, connectTimeMillis: Long = 0) =
        Call(id = id, state = state, number = id, connectTimeMillis = connectTimeMillis)

    @Test
    fun `primary call prefers incoming over active and holding`() {
        val calls = listOf(call("held", CallState.HOLDING), call("active", CallState.ACTIVE), call("ringing", CallState.INCOMING))

        assertEquals("ringing", primaryCall(calls)?.id)
    }

    @Test
    fun `primary call includes a call waiting for a phone account`() {
        assertEquals("pending", primaryCall(listOf(call("pending", CallState.SELECT_PHONE_ACCOUNT)))?.id)
    }

    @Test
    fun `primary call is null without calls`() {
        assertNull(primaryCall(emptyList()))
    }

    @Test
    fun `no calls is outgoing only while an outgoing call is pending`() {
        assertEquals(CallsState.NO_CALLS, callsState(emptyList(), hasPendingOutgoing = false))
        assertEquals(CallsState.OUTGOING, callsState(emptyList(), hasPendingOutgoing = true))
    }

    @Test
    fun `incoming next to an active call is extra incoming`() {
        assertEquals(CallsState.INCOMING, callsState(listOf(call("a", CallState.INCOMING)), false))
        assertEquals(
            CallsState.EXTRA_INCOMING,
            callsState(listOf(call("a", CallState.ACTIVE), call("b", CallState.INCOMING)), false)
        )
    }

    @Test
    fun `ongoing calls are active or active multi`() {
        assertEquals(CallsState.ACTIVE, callsState(listOf(call("a", CallState.ACTIVE)), false))
        assertEquals(
            CallsState.ACTIVE_MULTI,
            callsState(listOf(call("a", CallState.ACTIVE), call("b", CallState.HOLDING)), false)
        )
    }

    @Test
    fun `dialing connecting account selection and disconnected map to their states`() {
        assertEquals(CallsState.OUTGOING, callsState(listOf(call("a", CallState.DIALING)), false))
        assertEquals(CallsState.PENDING_OUTGOING, callsState(listOf(call("a", CallState.CONNECTING)), false))
        assertEquals(CallsState.WAITING_FOR_ACCOUNT, callsState(listOf(call("a", CallState.SELECT_PHONE_ACCOUNT)), false))
        assertEquals(CallsState.DISCONNECTED, callsState(listOf(call("a", CallState.DISCONNECTED)), false))
    }

    @Test
    fun `call has started only once connected outside setup states`() {
        assertTrue(call("a", CallState.ACTIVE, connectTimeMillis = 1).hasStarted)
        assertFalse(call("a", CallState.ACTIVE, connectTimeMillis = 0).hasStarted)
        assertFalse(call("a", CallState.DIALING, connectTimeMillis = 1).hasStarted)
    }
}
