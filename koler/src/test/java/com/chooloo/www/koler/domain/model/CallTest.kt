package com.chooloo.www.koler.domain.model

import com.chooloo.www.koler.domain.enum.CallState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CallTest {
    private fun call(state: CallState, connectTimeMillis: Long) =
        Call(id = "a", state = state, number = "a", connectTimeMillis = connectTimeMillis)

    @Test
    fun `call has started only once connected outside setup states`() {
        assertTrue(call(CallState.ACTIVE, connectTimeMillis = 1).hasStarted)
        assertFalse(call(CallState.ACTIVE, connectTimeMillis = 0).hasStarted)
        assertFalse(call(CallState.DIALING, connectTimeMillis = 1).hasStarted)
    }
}
