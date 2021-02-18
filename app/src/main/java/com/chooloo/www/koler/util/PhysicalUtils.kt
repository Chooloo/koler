package com.chooloo.www.koler.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

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

fun Activity.setShowWhenLocked() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }
}

fun Activity.disableKeyboard() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager).requestDismissKeyguard(this, null)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }
}