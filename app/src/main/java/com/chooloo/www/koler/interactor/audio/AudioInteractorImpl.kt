package com.chooloo.www.koler.interactor.audio

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.*
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import com.chooloo.www.koler.interactor.audio.AudioInteractor.AudioMode
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl
import java.util.*

class AudioInteractorImpl(
    private val vibrator: Vibrator,
    private val audioManager: AudioManager
) : BaseInteractorImpl<AudioInteractor.Listener>(), AudioInteractor {
    override var isMuted: Boolean
        get() = audioManager.isMicrophoneMute
        set(value) {
            audioManager.isMicrophoneMute = value
        }

    override var isSilent: Boolean
        get() = audioManager.ringerMode in arrayOf(RINGER_MODE_SILENT, RINGER_MODE_VIBRATE)
        set(value) {
            audioManager.ringerMode = if (value) RINGER_MODE_SILENT else RINGER_MODE_NORMAL
        }

    override var isSpeakerOn: Boolean
        get() = audioManager.isSpeakerphoneOn
        set(value) {
            audioManager.isSpeakerphoneOn = value
        }

    override var audioMode: AudioMode
        get() = AudioMode.values().associateBy(AudioMode::mode)
            .getOrDefault(audioManager.mode, AudioMode.NORMAL)
        set(value) {
            audioManager.mode = value.mode
        }


    override fun playTone(tone: Int) {
        playTone(tone, TONE_LENGTH_MS)
    }

    override fun playToneByKey(keyCode: Int) {
        playTone(sKeyToTone.getOrDefault(keyCode, -1))
    }

    override fun playTone(tone: Int, durationMs: Int) {
        if (tone != -1 && isSilent) {
            synchronized(sToneGeneratorLock) {
                ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME).startTone(
                    tone, durationMs
                ) // Start the new tone (will stop any playing tone)
            }
        }
    }


    override fun vibrate(millis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis, DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(millis)
        }
    }


    companion object {
        const val TONE_LENGTH_MS = 150 // The length of DTMF tones in milliseconds
        const val DIAL_TONE_STREAM_TYPE = android.media.AudioManager.STREAM_DTMF
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
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun requestCallFocus() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()
        val focusRequest =
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(attributes)
                .setWillPauseWhenDucked(false)
                .build()
        audioManager.requestAudioFocus(focusRequest)
    }
}