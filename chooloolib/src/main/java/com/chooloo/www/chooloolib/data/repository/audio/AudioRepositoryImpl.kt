package com.chooloo.www.chooloolib.data.repository.audio

import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import android.media.AudioManager.RINGER_MODE_VIBRATE
import android.media.AudioManager.STREAM_DTMF
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository.AudioMode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class AudioRepositoryImpl @Inject constructor(
    private val vibrator: Vibrator,
    private val audioManager: AudioManager
) : AudioRepository {
    private val toneGenerator: ToneGenerator? by lazy {
        try {
            ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME)
        } catch (_: RuntimeException) {
            null
        }
    }

    override suspend fun toggleMute(on: Boolean) = withContext(Dispatchers.IO) {
        audioManager.isMicrophoneMute = on
    }

    override suspend fun toggleSpeaker(on: Boolean): Unit = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (on) {
                audioManager.availableCommunicationDevices
                    .firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
                    ?.let { audioManager.setCommunicationDevice(it) }
            } else {
                audioManager.clearCommunicationDevice()
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager.isSpeakerphoneOn = on
        }
    }

    override suspend fun setAudioMode(audioMode: AudioMode) = withContext(Dispatchers.IO) {
        audioManager.mode = audioMode.mode
    }

    override suspend fun playTone(tone: Int) = withContext(Dispatchers.IO) {
        playTone(tone, TONE_LENGTH_MS)
    }

    override suspend fun playToneByChar(char: Char) {
        playTone(sCharToTone.getOrDefault(char, -1))
    }

    override suspend fun playTone(tone: Int, durationMs: Int) {
        if (tone != -1 && audioManager.ringerMode !in arrayOf(
                RINGER_MODE_SILENT,
                RINGER_MODE_VIBRATE
            )
        ) {
            synchronized(sToneGeneratorLock) {
                toneGenerator?.startTone(tone, durationMs)
            }
        }
    }

    override suspend fun vibrate(millis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis, DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(millis)
        }
    }

    companion object {
        const val TONE_LENGTH_MS = 150
        const val DIAL_TONE_STREAM_TYPE = STREAM_DTMF
        const val TONE_RELATIVE_VOLUME = 80

        private val sToneGeneratorLock = Any()
        private val sCharToTone = mapOf(
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
            '*' to ToneGenerator.TONE_DTMF_S
        )
    }
}
