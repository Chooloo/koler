package com.chooloo.www.chooloolib.ui.home

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeSearchTest {
    @Test
    fun `starts closed with no text`() {
        val search = HomeSearch(SavedStateHandle())

        assertFalse(search.isOpen.value)
        assertEquals("", search.text.value)
    }

    @Test
    fun `open and type keep the text`() {
        val search = HomeSearch(SavedStateHandle())

        search.open()
        search.changeText("dana")

        assertTrue(search.isOpen.value)
        assertEquals("dana", search.text.value)
    }

    @Test
    fun `close clears the text`() {
        val search = HomeSearch(SavedStateHandle())
        search.open()
        search.changeText("dana")

        search.close()

        assertFalse(search.isOpen.value)
        assertEquals("", search.text.value)
    }

    @Test
    fun `restores open search and text saved before process death`() {
        val saved = SavedStateHandle(mapOf("searching" to true, "search_text" to "dana"))

        val restored = HomeSearch(saved)

        assertTrue(restored.isOpen.value)
        assertEquals("dana", restored.text.value)
    }
}
