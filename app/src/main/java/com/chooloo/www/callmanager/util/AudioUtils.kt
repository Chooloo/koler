package com.chooloo.www.callmanager.util

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.KeyEvent
import java.util.*

object AudioUtils {
    private const val TONE_LENGTH_MS = 150 // The length of DTMF tones in milliseconds
    private const val TONE_LENGTH_INFINITE = -1
    private const val TONE_RELATIVE_VOLUME = 80 // The DTMF tone volume relative to other sounds in the stream
    private const val DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_DTMF // Stream type used to play the DTMF tones off call, and mapped to the volume control keys

    private val _toneGeneratorLock = Any()
    private var _toneGenerator: ToneGenerator? = null

    fun isPhoneSilent(activity: Activity): Boolean {
        val ringerMode = (activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager).ringerMode
        return ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE
    }

    fun toggleToneGenerator(toggle: Boolean) {
        synchronized(_toneGeneratorLock) {
            if (toggle && _toneGenerator == null) {
                try {
                    _toneGenerator = ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME)
                } catch (e: RuntimeException) {
                    _toneGenerator = null
                    e.printStackTrace()
                }
            } else {
                _toneGenerator?.release()
                _toneGenerator = null
            }
        }
    }

    /**
     * Play the specified tone for the specified milliseconds
     *
     *
     * The tone is played locally, using the audio stream for phone calls.
     * Tones are played only if the "Audible touch tones" user preference
     * is checked, and are NOT played if the device is in silent mode.
     *
     *
     * The tone length can be -1, meaning "keep playing the tone." If the caller does so, it should
     * call stopTone() afterward.
     *
     * @param tone       a tone code from [ToneGenerator]
     * @param durationMs tone length.
     */
    private fun playTone(tone: Int, durationMs: Int, activity: Activity) {
        if (tone == -1 || isPhoneSilent(activity)) {
            return
        }

        // We need to re-check the ringer mode for *every* playTone() call, rather than keeping
        // a local flag that's updated in onResume(), since it's possible to toggle silent mode
        // without leaving the current activity (via the ENDCALL-longpress menu.)
        synchronized(_toneGeneratorLock) {
            _toneGenerator?.startTone(tone, durationMs) // Start the new tone (will stop any playing tone)
        }
    }

    /**
     * Plays the specified tone for TONE_LENGTH_MS milliseconds.
     */
    private fun playTone(tone: Int, activity: Activity) {
        playTone(tone, TONE_LENGTH_MS, activity)
    }

    fun playToneByKey(keyCode: Int, activity: Activity) {
        val keyToTone = HashMap<Int, Int>()
        keyToTone[KeyEvent.KEYCODE_0] = ToneGenerator.TONE_DTMF_0
        keyToTone[KeyEvent.KEYCODE_1] = ToneGenerator.TONE_DTMF_1
        keyToTone[KeyEvent.KEYCODE_2] = ToneGenerator.TONE_DTMF_2
        keyToTone[KeyEvent.KEYCODE_3] = ToneGenerator.TONE_DTMF_3
        keyToTone[KeyEvent.KEYCODE_4] = ToneGenerator.TONE_DTMF_4
        keyToTone[KeyEvent.KEYCODE_5] = ToneGenerator.TONE_DTMF_5
        keyToTone[KeyEvent.KEYCODE_6] = ToneGenerator.TONE_DTMF_6
        keyToTone[KeyEvent.KEYCODE_7] = ToneGenerator.TONE_DTMF_7
        keyToTone[KeyEvent.KEYCODE_8] = ToneGenerator.TONE_DTMF_8
        keyToTone[KeyEvent.KEYCODE_9] = ToneGenerator.TONE_DTMF_9
        keyToTone[KeyEvent.KEYCODE_POUND] = ToneGenerator.TONE_DTMF_P
        keyToTone[KeyEvent.KEYCODE_STAR] = ToneGenerator.TONE_DTMF_S
        playTone(keyToTone.getOrDefault(keyCode, -1), activity)
    }

    /**
     * Stop the tone if it is played.
     * if local tone playback is disabled, just return.
     */
    fun stopTone() {
        synchronized(_toneGeneratorLock) {
            _toneGenerator?.stopTone()
        }
    }
}