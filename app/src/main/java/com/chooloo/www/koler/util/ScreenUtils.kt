package com.chooloo.www.koler.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.PowerManager
import android.view.MotionEvent
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


fun Activity.setShowWhenLocked() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    } else {
        window.addFlags(FLAG_SHOW_WHEN_LOCKED or FLAG_TURN_SCREEN_ON)
    }
}

fun Activity.disableKeyboard() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager).requestDismissKeyguard(
            this,
            null
        )
    } else {
        window.addFlags(FLAG_DISMISS_KEYGUARD)
    }
}

fun Activity.ignoreEditTextFocus(event: MotionEvent) {
    if (event.action == MotionEvent.ACTION_DOWN) {
        val v = currentFocus
        if (v is EditText) {
            val outRect = Rect()
            v.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                v.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }
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
