package com.chooloo.www.koler.util

import android.app.Activity
import android.content.Context.POWER_SERVICE
import android.os.PowerManager

class ProximitySensor(
        activity: Activity
) {
    private var _wakeLock: PowerManager.WakeLock
    private var _powerManager: PowerManager

    init {
        _powerManager = activity.getSystemService(POWER_SERVICE) as PowerManager
        _wakeLock = _powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, activity.localClassName)
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