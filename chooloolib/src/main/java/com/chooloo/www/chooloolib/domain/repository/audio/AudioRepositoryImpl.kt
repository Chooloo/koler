package com.chooloo.www.chooloolib.domain.repository.audio

import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import android.media.AudioManager.RINGER_MODE_VIBRATE
import android.media.AudioManager.STREAM_DTMF
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.KeyEvent
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository.AudioMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepositoryImpl @Inject constructor(
    private val vibrator: Vibrator,
    private val audioManager: AudioManager
) : BaseRepositoryImpl(), AudioRepository {
    override suspend fun toggleMute(on: Boolean) = withContext(Dispatchers.IO) {
        audioManager.isMicrophoneMute = on
    }

    override suspend fun toggleSpeaker(on: Boolean) = withContext(Dispatchers.IO) {
        audioManager.isSpeakerphoneOn = on
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

    override suspend fun playToneByKey(keyCode: Int) {
        playTone(sKeyToTone.getOrDefault(keyCode, -1))
    }

    override suspend fun playTone(tone: Int, durationMs: Int) {
        if (tone != -1 && audioManager.ringerMode !in arrayOf(
                RINGER_MODE_SILENT,
                RINGER_MODE_VIBRATE
            )
        ) {
            synchronized(sToneGeneratorLock) {
                ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME).startTone(
                    tone, durationMs
                ) // Start the new tone (will stop any playing tone)
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
        const val TONE_LENGTH_MS = 150 // The length of DTMF tones in milliseconds
        const val DIAL_TONE_STREAM_TYPE = STREAM_DTMF
        const val TONE_RELATIVE_VOLUME =
            80 // The DTMF tone volume relative to other sounds in the stream

        private val sToneGeneratorLock = Any()
        private val sKeyToTone by lazy {
            HashMap<Int, Int>().apply {
                put(KeyEvent.KEYCODE_0, ToneGenerator.TONE_DTMF_0)
                put(KeyEvent.KEYCODE_1, ToneGenerator.TONE_DTMF_1)
                put(KeyEvent.KEYCODE_2, ToneGenerator.TONE_DTMF_2)
                put(KeyEvent.KEYCODE_3, ToneGenerator.TONE_DTMF_3)
                put(KeyEvent.KEYCODE_4, ToneGenerator.TONE_DTMF_4)
                put(KeyEvent.KEYCODE_5, ToneGenerator.TONE_DTMF_5)
                put(KeyEvent.KEYCODE_6, ToneGenerator.TONE_DTMF_6)
                put(KeyEvent.KEYCODE_7, ToneGenerator.TONE_DTMF_7)
                put(KeyEvent.KEYCODE_8, ToneGenerator.TONE_DTMF_8)
                put(KeyEvent.KEYCODE_9, ToneGenerator.TONE_DTMF_9)
                put(KeyEvent.KEYCODE_POUND, ToneGenerator.TONE_DTMF_P)
                put(KeyEvent.KEYCODE_STAR, ToneGenerator.TONE_DTMF_S)
            }
        }
        private val sCharToTone by lazy {
            HashMap<Char, Int>().apply {
                put('0', ToneGenerator.TONE_DTMF_0)
                put('1', ToneGenerator.TONE_DTMF_1)
                put('2', ToneGenerator.TONE_DTMF_2)
                put('3', ToneGenerator.TONE_DTMF_3)
                put('4', ToneGenerator.TONE_DTMF_4)
                put('5', ToneGenerator.TONE_DTMF_5)
                put('6', ToneGenerator.TONE_DTMF_6)
                put('7', ToneGenerator.TONE_DTMF_7)
                put('8', ToneGenerator.TONE_DTMF_8)
                put('9', ToneGenerator.TONE_DTMF_9)
                put('#', ToneGenerator.TONE_DTMF_P)
                put('*', ToneGenerator.TONE_DTMF_S)
            }
        }
    }
}