package com.chooloo.www.chooloolib.domain.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.telecom.Call
import android.telecom.Call.AVAILABLE_PHONE_ACCOUNTS
import android.telecom.Call.STATE_DISCONNECTED
import android.telecom.CallAudioState
import android.telecom.DisconnectCause
import android.telecom.InCallService
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.ui.activity.CallActivity
import com.chooloo.www.chooloolib.ui.notification.CallNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@SuppressLint("NewApi")
@AndroidEntryPoint
class CallService : InCallService() {
    private val _job = SupervisorJob()
    private var _activity: CallActivity? = null
    private val _canAddCall = MutableStateFlow(false)
    private val _bringToForegroundEvent = MutableSharedFlow<Boolean>()
    private val _calls = MutableStateFlow<List<CallData>>(arrayListOf())
    private val _callAudioState = MutableStateFlow<CallAudioState?>(null)
    private val _pendingAccountHandle = MutableStateFlow<PhoneAccountHandle?>(null)

    val calls = _calls.asStateFlow()
    val canAddCall = _canAddCall.asStateFlow()
    val callAudioState = _callAudioState.asStateFlow()
    val pendingAccountHandle = _pendingAccountHandle.asStateFlow()
    val bringToForegroundEvent = _bringToForegroundEvent.asSharedFlow()

    @Inject lateinit var callNotification: CallNotification
    @Inject lateinit var preferenceRepository: PreferenceRepository


    override fun onCreate() {
        super.onCreate()
        _sInstance = this
        callNotification.attach()
    }

    override fun onDestroy() {
        callNotification.detach()
        super.onDestroy()
        _job.cancelChildren()
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        observeCall(call)
        onCallsChanged()
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        onCallsChanged()
    }

    /**
     * Bind means the service started
     * If intent has outgoing call extras and no calls, it means its an outgoing call
     */
    override fun onBind(intent: Intent?): IBinder? {
        val extras = intent?.getBundleExtra(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS)
        if (extras != null && extras.containsKey(AVAILABLE_PHONE_ACCOUNTS)) {
            _pendingAccountHandle.update { intent.getParcelableExtra(EXTRA_PHONE_ACCOUNT_HANDLE) }
        }
        return super.onBind(intent)
    }

    override fun onCanAddCallChanged(canAddCall: Boolean) {
        super.onCanAddCallChanged(canAddCall)
        _canAddCall.update { canAddCall }
    }

    /**
     * When this method is being called, we can be sure call isn't outgoing
     * Before we bring call to foreground we need to make sure
     * 1. Call activity isnt already active
     * 2. Service has calls
     */
    override fun onBringToForeground(showDialpad: Boolean) {
        super.onBringToForeground(showDialpad)
        _activity?.let { showCallUi(showDialpad) }
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        _callAudioState.update { audioState }
    }

    fun bindActivity(activity: CallActivity) {
        _activity?.finish()
        this._activity = activity
    }

    fun unbindActivity(activity: CallActivity) {
        if (_activity == activity) {
            _activity = null
        }
    }

    private fun showCallUi(showDialpad: Boolean) {
        if (preferenceRepository.incomingCallMode.value == PreferenceRepository.Companion.IncomingCallMode.FULL_SCREEN) {
            val intent = Intent(this, CallActivity::class.java).putExtra(
                CallActivity.SHOW_DIALPAD_EXTRA, showDialpad
            ).setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(this, intent, Bundle())
        }
    }

    private fun onCallsChanged() {
        _calls.update { getCalls().map(::CallData) }
    }

    private fun observeCall(call: Call) {
        call.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call?, state: Int) {
                onCallsChanged()
                if (state == STATE_DISCONNECTED && call?.details?.accountHandle == null && call?.details?.hasProperty(
                        Call.Details.PROPERTY_CONFERENCE
                    ) == false
                ) {
                    val accountHandles =
                        call.details?.extras?.getParcelableArrayList<PhoneAccountHandle>(
                            AVAILABLE_PHONE_ACCOUNTS
                        )

                    if (accountHandles.isNullOrEmpty()) {
                        val errorMsg =
                            applicationContext.getString(if (PhoneAccount.SCHEME_TEL === call.details?.handle?.scheme) R.string.error_call_failed_sim else R.string.error_call_service_unknown)
                        val disconnectCause =
                            DisconnectCause(DisconnectCause.ERROR, null, errorMsg, errorMsg)
                    }
                }
            }

            override fun onParentChanged(call: Call?, parent: Call?) {
                onCallsChanged()
            }

            override fun onDetailsChanged(call: Call?, details: Call.Details?) {
                onCallsChanged()
            }

            override fun onChildrenChanged(call: Call?, children: MutableList<Call>?) {
                onCallsChanged()
            }

            override fun onPostDialWait(call: Call?, remainingPostDialSequence: String?) {
                onCallsChanged()
            }

            override fun onVideoCallChanged(call: Call?, videoCall: VideoCall?) {
                onCallsChanged()
            }

            override fun onCallDestroyed(call: Call?) {
                onCallsChanged()
            }

            override fun onCannedTextResponsesLoaded(
                call: Call?, cannedTextResponses: MutableList<String>?
            ) {
                onCallsChanged()
            }

            override fun onConferenceableCallsChanged(
                call: Call?, conferenceableCalls: MutableList<Call>?
            ) {
                onCallsChanged()
            }
        })
    }

    companion object {
        private var _sInstance: CallService? = null
        val sInstance get() = _sInstance
    }
}