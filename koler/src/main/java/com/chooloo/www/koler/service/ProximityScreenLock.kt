package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager
import android.util.Log
import com.chooloo.www.chooloolib.di.ApplicationScope
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.domain.ProximityScreenOff
import com.chooloo.www.koler.domain.proximityScreenOff
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Singleton
class ProximityScreenLock @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val callRepository: CallRepository,
) {
    private val wakeLock: PowerManager.WakeLock? = createWakeLock()
    private var job: Job? = null

    fun attach() {
        job?.cancel()
        job = applicationScope.launch(Dispatchers.Main.immediate) {
            combine(callRepository.calls, callRepository.audio) { calls, audio ->
                proximityScreenOff(calls, audio.route)
            }.distinctUntilChanged().collect(::update)
        }
    }

    fun detach() {
        job?.cancel()
        job = null
        update(ProximityScreenOff.RELEASE_WHEN_UNCOVERED)
    }

    // Held for the whole call, which has no upper time limit; released when the call leaves the ear or ends.
    @SuppressLint("WakelockTimeout")
    private fun update(screenOff: ProximityScreenOff) {
        when (screenOff) {
            ProximityScreenOff.HOLD -> wakeLock?.acquire()
            ProximityScreenOff.RELEASE_NOW -> wakeLock?.release()
            ProximityScreenOff.RELEASE_WHEN_UNCOVERED ->
                wakeLock?.release(PowerManager.RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY)
        }
    }

    private fun createWakeLock(): PowerManager.WakeLock? {
        val powerManager = context.getSystemService(PowerManager::class.java)
        if (!powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            Log.w(TAG, "This device has no proximity screen-off support")
            return null
        }
        return powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, WAKE_LOCK_TAG)
            .apply { setReferenceCounted(false) }
    }

    private companion object {
        const val TAG = "ProximityScreenLock"
        const val WAKE_LOCK_TAG = "koler:proximity"
    }
}
