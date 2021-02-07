package com.chooloo.www.koler.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

const val SHORT_VIBRATE_LENGTH: Long = 20
const val DEFAULT_VIBRATE_LENGTH: Long = 100

fun Context.vibrate(millis: Long = DEFAULT_VIBRATE_LENGTH) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(millis)
    }
}
