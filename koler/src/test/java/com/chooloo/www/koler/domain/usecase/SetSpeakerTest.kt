package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.ui.call.FakeCallRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class SetSpeakerTest {
    private val callRepository = FakeCallRepository()
    private val setSpeaker = SetSpeaker(callRepository)

    @Test
    fun `speaker on routes to the speaker`() {
        setSpeaker(true)

        assertEquals(listOf("route SPEAKER"), callRepository.actions)
    }

    @Test
    fun `speaker off returns to wired or earpiece`() {
        callRepository.audio.value = CallAudio(route = AudioRoute.SPEAKER)

        setSpeaker(false)

        assertEquals(listOf("route WIRED_OR_EARPIECE"), callRepository.actions)
    }

    @Test
    fun `speaker off keeps bluetooth`() {
        callRepository.audio.value = CallAudio(route = AudioRoute.BLUETOOTH)

        setSpeaker(false)

        assertEquals(emptyList<String>(), callRepository.actions)
    }
}
