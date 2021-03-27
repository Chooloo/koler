package com.chooloo.www.koler.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


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
        (getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager).requestDismissKeyguard(
            this,
            null
        )
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }
}

class ProximitySensor(
    activity: Activity
) {
    private val _powerManager by lazy { activity.getSystemService(Context.POWER_SERVICE) as PowerManager }
    private val _wakeLock by lazy {
        _powerManager.newWakeLock(
            PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,
            activity.localClassName
        )
    }

    fun acquire() {
        if (!_wakeLock.isHeld) {
            _wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
        }
    }

    fun release() {
        if (_wakeLock.isHeld) {
            _wakeLock.release()
        }
    }
}