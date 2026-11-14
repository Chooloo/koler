package com.chooloo.www.koler.domain.enum

import org.junit.Assert.assertEquals
import org.junit.Test

class CallsStateTest {
    @Test
    fun `actions show only for active, multi and outgoing calls`() {
        assertEquals(
            listOf(CallsState.OUTGOING, CallsState.ACTIVE, CallsState.ACTIVE_MULTI),
            CallsState.entries.filter { it.showsActions },
        )
    }

    @Test
    fun `calls are ended only when disconnected or gone`() {
        assertEquals(
            listOf(CallsState.NO_CALLS, CallsState.DISCONNECTED),
            CallsState.entries.filter { it.isEnded },
        )
    }
}
