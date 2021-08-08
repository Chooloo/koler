package com.chooloo.www.koler.util

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import com.chooloo.www.koler.service.CallService
import java.util.*

class AudioManager(private val context: Context) {
    private val _audioManager by lazy { context.getSystemService(AUDIO_SERVICE) as AudioManager }

    val isPhoneSilent: Boolean
        get() = _audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT ||
                _audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE

    var audioMode: AudioMode
        get() = AudioMode.values().associateBy(AudioMode::mode)
            .getOrDefault(_audioManager.mode, AudioMode.NORMAL)
        set(value) {
            _audioManager.mode = value.mode
        }

    var isSpeakerOn: Boolean
        get() = _audioManager.isSpeakerphoneOn
        set(value) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
                CallService.toggleSpeakerRoute(value)
            } else {
                audioMode = AudioMode.IN_CALL
                _audioManager.isSpeakerphoneOn = value
            }
        }

    var isMuted: Boolean
        get() = _audioManager.isMicrophoneMute
        set(value) {
            _audioManager.isMicrophoneMute = value
        }


    /**
     * Play the specified tone for the specified milliseconds
     *
     *
     * The tone is played locally, using the audio stream for phone calls.
     * Tones are played only if the "Audible touch tones" user preference
     * is checked, and are NOT played if the device is in silent mode.
     *
     * The tone length can be -1, meaning "keep playing the tone." If the caller does so, it should
     * call stopTone() afterward.
     *
     * We need to re-check the ringer mode for *every* playTone() call, rather than keeping
     * a local flag that's updated in onResume(), since it's possible to toggle silent mode
     * without leaving the current activity (via the ENDCALL-longpress menu.)
     */
    fun playTone(tone: Int, durationMs: Int) {
        if (tone != -1 && isPhoneSilent) {
            synchronized(toneGeneratorLock) {
                ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME).startTone(
                    tone, durationMs
                ) // Start the new tone (will stop any playing tone)
            }
        }
    }

    fun playTone(tone: Int) {
        playTone(tone, TONE_LENGTH_MS)
    }

    fun playToneByKey(keyCode: Int) {
        playTone(keyToTone.getOrDefault(keyCode, -1))
    }

    fun vibrate(millis: Long = DEFAULT_VIBRATE_LENGTH) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    millis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(millis)
        }
    }


    enum class AudioMode(val mode: Int) {
        NORMAL(AudioManager.MODE_NORMAL),
        IN_CALL(AudioManager.MODE_IN_CALL),
        CURRENT(AudioManager.MODE_CURRENT),
        RINGTONE(AudioManager.MODE_RINGTONE),
        IN_COMMUNICATION(AudioManager.MODE_IN_COMMUNICATION)
    }

    companion object {
        const val TONE_LENGTH_MS = 150 // The length of DTMF tones in milliseconds
        const val TONE_RELATIVE_VOLUME = 80 // The DTMF tone volume relative to other sounds in the stream
        const val SHORT_VIBRATE_LENGTH: Long = 20
        const val DEFAULT_VIBRATE_LENGTH: Long = 100
        const val DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_DTMF

        val toneGeneratorLock = Any()
        val keyToTone by lazy {
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
}