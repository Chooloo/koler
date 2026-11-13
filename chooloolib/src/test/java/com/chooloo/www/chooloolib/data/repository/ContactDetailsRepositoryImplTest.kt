package com.chooloo.www.chooloolib.data.repository

import com.chooloo.www.chooloolib.data.repository.ContactDetailsRepositoryImpl.Companion.whatsappNumberOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ContactDetailsRepositoryImplTest {
    @Test
    fun `whatsapp row keeps only the digits before the domain`() {
        assertEquals("972501234567", whatsappNumberOf("972501234567@s.whatsapp.net"))
    }

    @Test
    fun `formatted number keeps only its digits`() {
        assertEquals("972501234567", whatsappNumberOf("+972 50-123-4567"))
    }

    @Test
    fun `row without digits has no number`() {
        assertNull(whatsappNumberOf("@s.whatsapp.net"))
    }
}
