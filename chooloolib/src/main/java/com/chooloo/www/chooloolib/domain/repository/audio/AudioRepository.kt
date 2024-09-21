package com.chooloo.www.chooloolib.domain.repository.audio

import android.media.AudioManager
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface AudioRepository : BaseRepository {
    suspend fun toggleMute(on: Boolean)
    suspend fun toggleSpeaker(on: Boolean)
    suspend fun setAudioMode(audioMode: AudioMode)

    suspend fun playTone(tone: Int)
    suspend fun playToneByChar(char: Char)
    suspend fun playToneByKey(keyCode: Int)
    suspend fun playTone(tone: Int, durationMs: Int)

    suspend fun vibrate(millis: Long = 10)

    enum class AudioMode(val mode: Int) {
        NORMAL(AudioManager.MODE_NORMAL),
        IN_CALL(AudioManager.MODE_IN_CALL),
        CURRENT(AudioManager.MODE_CURRENT),
        RINGTONE(AudioManager.MODE_RINGTONE),
        IN_COMMUNICATION(AudioManager.MODE_IN_COMMUNICATION)
    }

    companion object {
        const val SHORT_VIBRATE_LENGTH: Long = 20
    }
}