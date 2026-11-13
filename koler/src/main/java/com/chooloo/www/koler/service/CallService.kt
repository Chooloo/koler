package com.chooloo.www.koler.service

import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import android.telecom.TelecomManager
import com.chooloo.www.koler.data.repository.CallRepositoryImpl
import com.chooloo.www.koler.device.ProximityScreenLock
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.notification.CallNotification
import com.chooloo.www.koler.ui.call.CallActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallService : InCallService() {
    @Inject lateinit var callRepository: CallRepositoryImpl

    @Inject lateinit var callNotification: CallNotification

    @Inject lateinit var proximityScreenLock: ProximityScreenLock

    override fun onCreate() {
        super.onCreate()
        callRepository.onServiceCreated(this)
        callNotification.attach(this)
        proximityScreenLock.attach()
    }

    override fun onDestroy() {
        proximityScreenLock.detach()
        callNotification.detach(this)
        callRepository.onServiceDestroyed(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        val extras = intent?.getBundleExtra(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS)
        if (extras?.containsKey(Call.AVAILABLE_PHONE_ACCOUNTS) == true) {
            callRepository.onPendingOutgoing()
        }
        return super.onBind(intent)
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        val addedCall = callRepository.onCallAdded(call)
        // The framework reports only changes from its own starting value, so read the current value.
        callRepository.onCanAddCallChanged(canAddCall())
        if (addedCall.state != CallState.INCOMING ||
            (calls.size == 1 && !isPhoneInUse() && !isDoNotDisturbOn())
        ) {
            showCallUi(showDialpad = false)
        }
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        callRepository.onCallRemoved(call)
    }

    override fun onCanAddCallChanged(canAddCall: Boolean) {
        super.onCanAddCallChanged(canAddCall)
        callRepository.onCanAddCallChanged(canAddCall)
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        callRepository.onAudioStateChanged(audioState)
    }

    override fun onBringToForeground(showDialpad: Boolean) {
        super.onBringToForeground(showDialpad)
        showCallUi(showDialpad)
    }

    private fun isPhoneInUse(): Boolean =
        getSystemService(PowerManager::class.java).isInteractive &&
            !getSystemService(KeyguardManager::class.java).isKeyguardLocked

    private fun isDoNotDisturbOn(): Boolean =
        getSystemService(NotificationManager::class.java).currentInterruptionFilter !=
            NotificationManager.INTERRUPTION_FILTER_ALL

    private fun showCallUi(showDialpad: Boolean) {
        startActivity(
            Intent(this, CallActivity::class.java)
                .putExtra(CallActivity.EXTRA_SHOW_DIALPAD, showDialpad)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}
