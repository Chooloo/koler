package com.chooloo.www.koler.util

import android.app.Activity
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.os.PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK

class ProximitySensor(
    activity: Activity
) {
    private val _powerManager by lazy { activity.getSystemService(POWER_SERVICE) as PowerManager }
    private val _wakeLock by lazy {
        _powerManager.newWakeLock(PROXIMITY_SCREEN_OFF_WAKE_LOCK, activity.localClassName)
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