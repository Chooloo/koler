package com.chooloo.www.chooloolib.domain.repository.proximity

import android.os.PowerManager
import android.os.PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProximityRepositoryImpl @Inject constructor(
    private val powerManager: PowerManager
) : BaseRepositoryImpl(), ProximityRepository {
    private val _wakeLock by lazy {
        powerManager.newWakeLock(PROXIMITY_SCREEN_OFF_WAKE_LOCK, "koler:screen_wake_lock")
    }

    override fun acquire() {
        if (!_wakeLock.isHeld) {
            _wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
        }
    }

    override fun release() {
        if (_wakeLock.isHeld) {
            _wakeLock.release()
        }
    }
}