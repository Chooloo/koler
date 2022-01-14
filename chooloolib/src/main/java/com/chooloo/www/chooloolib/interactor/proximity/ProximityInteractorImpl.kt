package com.chooloo.www.chooloolib.interactor.proximity

import android.os.PowerManager
import android.os.PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable

class ProximityInteractorImpl(
    private val activity: BaseActivity,
    private val powerManager: PowerManager
) : BaseObservable<ProximityInteractor.Listener>(),
    ProximityInteractor {

    private val _wakeLock by lazy {
        powerManager.newWakeLock(
            PROXIMITY_SCREEN_OFF_WAKE_LOCK,
            activity.localClassName
        )
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