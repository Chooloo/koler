package com.chooloo.www.chooloolib.ui.dialpad

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.testing.FakeAudioRepository
import com.chooloo.www.chooloolib.testing.FakePreferenceRepository
import com.chooloo.www.chooloolib.testing.FakeRecentRepository
import com.chooloo.www.chooloolib.testing.FakeTelecomRepository
import com.chooloo.www.chooloolib.testing.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DialpadViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val audio = FakeAudioRepository()
    private val telecom = FakeTelecomRepository()
    private val recents = FakeRecentRepository(lastOutgoingNumber = "0501234567")

    private fun viewModel(settings: Settings = Settings()) =
        DialpadViewModel(audio, recents, telecom, FakePreferenceRepository(settings))

    @Test
    fun `set number keeps only dialable characters`() {
        val viewModel = viewModel()

        viewModel.setNumber("+972 (50) 123-45#*")

        assertEquals("+9725012345#*", viewModel.uiState.value.value)
    }

    @Test
    fun `key click appends key and plays enabled feedback`() {
        val viewModel = viewModel(Settings(isDialpadTonesEnabled = true, isDialpadVibrateEnabled = false))

        viewModel.onKeyClick('1')
        viewModel.onKeyClick('2')

        assertEquals("12", viewModel.uiState.value.value)
        assertEquals(listOf('1', '2'), audio.playedTones)
        assertEquals(0, audio.vibrations)
        assertEquals(listOf("1", "12"), telecom.secretCodes)
    }

    @Test
    fun `delete removes last character and long delete clears`() {
        val viewModel = viewModel()
        viewModel.setNumber("123")

        viewModel.onDeleteClick()
        assertEquals("12", viewModel.uiState.value.value)

        viewModel.onDeleteLongClick()
        assertEquals("", viewModel.uiState.value.value)
    }

    @Test
    fun `call with a number places the call`() {
        val viewModel = viewModel()
        viewModel.setNumber("123")

        viewModel.onCallClick()

        assertEquals(listOf("123"), telecom.calledNumbers)
    }

    @Test
    fun `failed call reports an error message`() {
        val viewModel = viewModel()
        telecom.callError = SecurityException()
        viewModel.setNumber("123")

        viewModel.onCallClick()
        assertEquals(R.string.error_call_failed, viewModel.uiState.value.message)

        viewModel.onMessageShown()
        assertEquals(null, viewModel.uiState.value.message)
    }

    @Test
    fun `call with an empty number fills the last outgoing number`() {
        val viewModel = viewModel()

        viewModel.onCallClick()

        assertEquals("0501234567", viewModel.uiState.value.value)
        assertEquals(emptyList<String>(), telecom.calledNumbers)
    }

    @Test
    fun `long press zero adds plus and long press one calls voicemail`() {
        val viewModel = viewModel()

        viewModel.onKeyLongClick('0')
        viewModel.onKeyLongClick('1')

        assertEquals("+", viewModel.uiState.value.value)
        assertEquals(1, telecom.voicemailCalls)
    }
}
