package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.repository.AudioRepository

class FakeAudioRepository : AudioRepository {
    val playedTones = mutableListOf<Char>()
    var vibrations = 0

    override suspend fun playTone(key: Char) {
        playedTones += key
    }

    override suspend fun vibrate() {
        vibrations++
    }
}
