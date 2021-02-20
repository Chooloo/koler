package com.chooloo.www.koler.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.KeyEvent
import java.util.*

// Stream type used to play the DTMF tones off call, and mapped to the volume control keys
private const val DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_DTMF

// The DTMF tone volume relative to other sounds in the stream
private const val TONE_RELATIVE_VOLUME = 80

// The length of DTMF tones in milliseconds
private const val TONE_LENGTH_MS = 150

val toneGeneratorLock = Any()

fun Context.isPhoneSilent(): Boolean {
    val ringerMode = (getSystemService(Context.AUDIO_SERVICE) as AudioManager).ringerMode
    return ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE
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
 * We need to re-check the ringer mode for *every* playTone() call, rather than keeping
 * a local flag that's updated in onResume(), since it's possible to toggle silent mode
 * without leaving the current activity (via the ENDCALL-longpress menu.)
 * @param tone       a tone code from [ToneGenerator]
 * @param durationMs tone length.
 */
fun Context.playTone(tone: Int, durationMs: Int) {
    if (tone != -1 && isPhoneSilent()) {
        synchronized(toneGeneratorLock) {
            ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME).startTone(
                tone,
                durationMs
            ) // Start the new tone (will stop any playing tone)
        }
    }
}

fun Context.playTone(tone: Int) {
    playTone(tone, TONE_LENGTH_MS)
}

fun Context.playToneByKey(keyCode: Int) {
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
    playTone(keyToTone.getOrDefault(keyCode, -1))
}