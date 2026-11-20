package com.chooloo.www.chooloolib.ui.dialpad

import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.testing.FakeAudioRepository
import com.chooloo.www.chooloolib.testing.FakePreferenceRepository
import com.chooloo.www.chooloolib.testing.FakeRecentRepository
import com.chooloo.www.chooloolib.testing.FakeTelecomRepository
import com.chooloo.www.chooloolib.testing.MainDispatcherRule
import com.chooloo.www.chooloolib.ui.common.UserMessage
import kotlinx.coroutines.test.runTest
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

        assertEquals("+9725012345#*", viewModel.uiState.value.input.text)
    }

    @Test
    fun `key click appends key and plays enabled feedback`() {
        val viewModel =
            viewModel(Settings(isDialpadTonesEnabled = true, isDialpadVibrateEnabled = false))

        viewModel.onKeyClick('1')
        viewModel.onKeyClick('2')

        assertEquals("12", viewModel.uiState.value.input.text)
        assertEquals(listOf('1', '2'), audio.playedTones)
        assertEquals(0, audio.vibrations)
        assertEquals(listOf("1", "12"), telecom.secretCodes)
    }

    @Test
    fun `failed secret code keeps the typed value`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = viewModel()
        telecom.secretCodeError = SecurityException()

        viewModel.onKeyClick('1')

        assertEquals("1", viewModel.uiState.value.input.text)
    }

    @Test
    fun `delete removes last character and long delete clears`() {
        val viewModel = viewModel()
        viewModel.setNumber("123")

        viewModel.onDeleteClick()
        assertEquals("12", viewModel.uiState.value.input.text)

        viewModel.onDeleteLongClick()
        assertEquals("", viewModel.uiState.value.input.text)
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
        assertEquals(UserMessage.CALL_FAILED, viewModel.uiState.value.message)

        viewModel.onMessageShown()
        assertEquals(null, viewModel.uiState.value.message)
    }

    @Test
    fun `call with an empty number fills the last outgoing number`() {
        val viewModel = viewModel()

        viewModel.onCallClick()

        assertEquals("0501234567", viewModel.uiState.value.input.text)
        assertEquals(emptyList<String>(), telecom.calledNumbers)
    }

    @Test
    fun `long press zero adds plus and long press one calls voicemail`() {
        val viewModel = viewModel()

        viewModel.onKeyLongClick('0')
        viewModel.onKeyLongClick('1')

        assertEquals("+", viewModel.uiState.value.input.text)
        assertEquals(1, telecom.voicemailCalls)
    }

    @Test
    fun `key click inserts at the cursor and replaces a selection`() {
        val viewModel = viewModel()
        viewModel.onInputChange(DialpadInput("1234", 2))

        viewModel.onKeyClick('9')
        assertEquals(DialpadInput("12934", 3), viewModel.uiState.value.input)

        viewModel.onInputChange(DialpadInput("12934", 1, 4))
        viewModel.onKeyClick('5')
        assertEquals(DialpadInput("154", 2), viewModel.uiState.value.input)
    }

    @Test
    fun `delete removes before the cursor or the selection and does nothing at the start`() {
        val viewModel = viewModel()
        viewModel.onInputChange(DialpadInput("1234", 2))

        viewModel.onDeleteClick()
        assertEquals(DialpadInput("134", 1), viewModel.uiState.value.input)

        viewModel.onInputChange(DialpadInput("134", 0))
        viewModel.onDeleteClick()
        assertEquals(DialpadInput("134", 0), viewModel.uiState.value.input)

        viewModel.onInputChange(DialpadInput("134", 1, 2))
        viewModel.onDeleteClick()
        assertEquals(DialpadInput("14", 1), viewModel.uiState.value.input)
    }

    @Test
    fun `long press zero adds plus at the cursor`() {
        val viewModel = viewModel()
        viewModel.onInputChange(DialpadInput("0501234567", 0))

        viewModel.onKeyLongClick('0')

        assertEquals(DialpadInput("+0501234567", 1), viewModel.uiState.value.input)
    }

    @Test
    fun `paste keeps only dialable characters and puts the cursor after them`() {
        val viewModel = viewModel()

        viewModel.onInputChange(DialpadInput("12(050) 1-800-ab34", 16))

        assertEquals(DialpadInput("12050180034", 9), viewModel.uiState.value.input)
    }

    @Test
    fun `set number puts the cursor at the end`() {
        val viewModel = viewModel()

        viewModel.setNumber("050 123")

        assertEquals(DialpadInput("050123", 6), viewModel.uiState.value.input)
    }

    @Test
    fun `key click replaces a reversed selection`() {
        val viewModel = viewModel()
        viewModel.onInputChange(DialpadInput("12345", 4, 1))

        viewModel.onKeyClick('9')

        assertEquals(DialpadInput("195", 2), viewModel.uiState.value.input)
    }
}
