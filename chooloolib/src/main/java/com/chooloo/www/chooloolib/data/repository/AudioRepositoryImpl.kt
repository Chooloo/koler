package com.chooloo.www.chooloolib.data.repository

import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_NORMAL
import android.media.AudioManager.STREAM_DTMF
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepositoryImpl @Inject constructor(
    private val vibrator: Vibrator,
    private val audioManager: AudioManager,
) : AudioRepository {
    // ToneGenerator init throws on some devices while the audio HAL is busy; dialing works without tones.
    private val toneGenerator: ToneGenerator? by lazy {
        runCatching { ToneGenerator(STREAM_DTMF, TONE_VOLUME) }
            .onFailure { Log.w(TAG, "Dial tones unavailable", it) }
            .getOrNull()
    }

    override suspend fun playDtmfTone(key: Char) {
        val tone = DTMF_TONES[key] ?: return
        if (audioManager.ringerMode == RINGER_MODE_NORMAL) {
            toneGenerator?.startTone(tone, TONE_LENGTH_MS)
        }
    }

    override suspend fun vibrate() {
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
    }

    private companion object {
        const val TAG = "AudioRepository"
        const val TONE_VOLUME = 80
        const val TONE_LENGTH_MS = 150
        val DTMF_TONES = mapOf(
            '0' to ToneGenerator.TONE_DTMF_0,
            '1' to ToneGenerator.TONE_DTMF_1,
            '2' to ToneGenerator.TONE_DTMF_2,
            '3' to ToneGenerator.TONE_DTMF_3,
            '4' to ToneGenerator.TONE_DTMF_4,
            '5' to ToneGenerator.TONE_DTMF_5,
            '6' to ToneGenerator.TONE_DTMF_6,
            '7' to ToneGenerator.TONE_DTMF_7,
            '8' to ToneGenerator.TONE_DTMF_8,
            '9' to ToneGenerator.TONE_DTMF_9,
            '#' to ToneGenerator.TONE_DTMF_P,
            '*' to ToneGenerator.TONE_DTMF_S,
        )
    }
}
