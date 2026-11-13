package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetPrimaryCallTest {
    private val getPrimaryCall = GetPrimaryCall()

    private fun call(id: String, state: CallState) = Call(id = id, state = state, number = id)

    @Test
    fun `primary call prefers incoming over active and holding`() {
        val calls = listOf(
            call("held", CallState.HOLDING),
            call("active", CallState.ACTIVE),
            call("ringing", CallState.INCOMING),
        )

        assertEquals("ringing", getPrimaryCall(calls)?.id)
    }

    @Test
    fun `primary call includes a call waiting for a phone account`() {
        assertEquals(
            "pending",
            getPrimaryCall(listOf(call("pending", CallState.SELECT_PHONE_ACCOUNT)))?.id,
        )
    }

    @Test
    fun `primary call is null without calls`() {
        assertNull(getPrimaryCall(emptyList()))
    }
}
