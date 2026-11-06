package com.chooloo.www.koler.ui.call

import android.content.ContextWrapper
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.domain.CallState
import org.junit.Assert.assertEquals
import org.junit.Test

class CallLabelsTest {
    @Test
    fun `no caller leaves the phone number as the title`() {
        val call = Call(id = "c1", state = CallState.INCOMING, number = "111")

        assertEquals("111", call.title(ContextWrapper(null), null))
    }
}
