package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.enum.CallsState
import com.chooloo.www.koler.domain.model.Call
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCallsStateTest {
    private val getCallsState = GetCallsState()

    private fun call(id: String, state: CallState) = Call(id = id, state = state, number = id)

    @Test
    fun `no calls is outgoing only while an outgoing call is pending`() {
        assertEquals(CallsState.NO_CALLS, getCallsState(emptyList(), hasPendingOutgoing = false))
        assertEquals(CallsState.OUTGOING, getCallsState(emptyList(), hasPendingOutgoing = true))
    }

    @Test
    fun `any ringing call is incoming`() {
        assertEquals(
            CallsState.INCOMING,
            getCallsState(listOf(call("a", CallState.INCOMING)), false),
        )
        assertEquals(
            CallsState.INCOMING,
            getCallsState(
                listOf(call("a", CallState.ACTIVE), call("b", CallState.INCOMING)),
                false,
            ),
        )
    }

    @Test
    fun `ongoing calls are active or active multi`() {
        assertEquals(CallsState.ACTIVE, getCallsState(listOf(call("a", CallState.ACTIVE)), false))
        assertEquals(
            CallsState.ACTIVE_MULTI,
            getCallsState(listOf(call("a", CallState.ACTIVE), call("b", CallState.HOLDING)), false),
        )
    }

    @Test
    fun `dialing connecting account selection and disconnected map to their states`() {
        assertEquals(
            CallsState.OUTGOING,
            getCallsState(listOf(call("a", CallState.DIALING)), false),
        )
        assertEquals(
            CallsState.OUTGOING,
            getCallsState(listOf(call("a", CallState.CONNECTING)), false),
        )
        assertEquals(
            CallsState.OUTGOING,
            getCallsState(
                listOf(call("a", CallState.CONNECTING), call("b", CallState.DIALING)),
                false,
            ),
        )
        assertEquals(
            CallsState.WAITING_FOR_ACCOUNT,
            getCallsState(listOf(call("a", CallState.SELECT_PHONE_ACCOUNT)), false),
        )
        assertEquals(
            CallsState.DISCONNECTED,
            getCallsState(listOf(call("a", CallState.DISCONNECTED)), false),
        )
    }
}
