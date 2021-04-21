package com.chooloo.www.koler.util

import android.content.Context
import android.media.AudioManager.STREAM_DTMF
import android.media.ToneGenerator
import android.media.ToneGenerator.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent.*
import com.chooloo.www.koler.util.audio.AudioManager
import java.util.*

// Stream type used to play the DTMF tones off call, and mapped to the volume control keys
const val DIAL_TONE_STREAM_TYPE = STREAM_DTMF
const val TONE_RELATIVE_VOLUME = 80 // The DTMF tone volume relative to other sounds in the stream
const val TONE_LENGTH_MS = 150 // The length of DTMF tones in milliseconds
const val SHORT_VIBRATE_LENGTH: Long = 20
const val DEFAULT_VIBRATE_LENGTH: Long = 100

val toneGeneratorLock = Any()
val keyToTone by lazy {
    HashMap<Int, Int>().apply {
        put(KEYCODE_0, TONE_DTMF_0)
        put(KEYCODE_1, TONE_DTMF_1)
        put(KEYCODE_2, TONE_DTMF_2)
        put(KEYCODE_3, TONE_DTMF_3)
        put(KEYCODE_4, TONE_DTMF_4)
        put(KEYCODE_5, TONE_DTMF_5)
        put(KEYCODE_6, TONE_DTMF_6)
        put(KEYCODE_7, TONE_DTMF_7)
        put(KEYCODE_8, TONE_DTMF_8)
        put(KEYCODE_9, TONE_DTMF_9)
        put(KEYCODE_POUND, TONE_DTMF_P)
        put(KEYCODE_STAR, TONE_DTMF_S)
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
 * We need to re-check the ringer mode for *every* playTone() call, rather than keeping
 * a local flag that's updated in onResume(), since it's possible to toggle silent mode
 * without leaving the current activity (via the ENDCALL-longpress menu.)
 * @param tone       a tone code from [ToneGenerator]
 * @param durationMs tone length.
 */
fun Context.playTone(tone: Int, durationMs: Int) {
    if (tone != -1 && AudioManager(this).isPhoneSilent) {
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
    playTone(keyToTone.getOrDefault(keyCode, -1))
}


fun Context.vibrate(millis: Long = DEFAULT_VIBRATE_LENGTH) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(millis)
    }
}

