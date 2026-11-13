package com.chooloo.www.koler.data.repository

import android.telecom.TelecomManager
import com.chooloo.www.koler.data.mapper.CallMapper
import com.chooloo.www.koler.domain.enum.AudioRoute
import org.junit.Test

class CallRepositoryImplTest {
    private val repository = CallRepositoryImpl(
        CallMapper(
            TelecomManager::class.java.getDeclaredConstructor().apply {
                isAccessible = true
            }.newInstance(),
        ),
    )

    @Test
    fun `mute with no bound service does not throw`() {
        repository.setMuted(true)
    }

    @Test
    fun `audio route with no bound service does not throw`() {
        repository.setAudioRoute(AudioRoute.SPEAKER)
    }
}
