package com.chooloo.www.koler.ui.common

import android.content.ContextWrapper
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import org.junit.Assert.assertEquals
import org.junit.Test

class CallLabelsTest {
    @Test
    fun `no caller leaves the phone number, kept left to right, as the title`() {
        val call = Call(id = "c1", state = CallState.INCOMING, number = "111")

        assertEquals("\u2066111\u2069", CallLabels.title(call, ContextWrapper(null), null))
    }
}
