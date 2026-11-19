package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.CallAlert
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCallAlertTest {
    private val getCallAlert = GetCallAlert()

    private fun call(state: CallState) = Call(id = "a", state = state, number = "a")

    @Test
    fun `incoming call rings while the call screen is not shown`() {
        assertEquals(CallAlert.RING, getCallAlert(call(CallState.INCOMING), false))
    }

    @Test
    fun `incoming call is silent while the call screen is shown`() {
        assertEquals(CallAlert.SILENT, getCallAlert(call(CallState.INCOMING), true))
    }

    @Test
    fun `other calls do not alert whether or not the call screen is shown`() {
        CallState.entries.filter { it != CallState.INCOMING }.forEach { state ->
            assertEquals(CallAlert.NONE, getCallAlert(call(state), false))
            assertEquals(CallAlert.NONE, getCallAlert(call(state), true))
        }
    }
}
