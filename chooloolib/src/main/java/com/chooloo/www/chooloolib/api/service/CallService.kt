package com.chooloo.www.chooloolib.api.service

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.CallAudioState
import android.telecom.InCallService
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.data.model.Call
import com.chooloo.www.chooloolib.notification.CallNotification
import com.chooloo.www.chooloolib.data.repository.calls.CallsRepository
import com.chooloo.www.chooloolib.ui.call.CallActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@SuppressLint("NewApi")
@AndroidEntryPoint
class CallService : InCallService() {
    private val _calls = MutableStateFlow<List<Call>>(arrayListOf())

    val calls = _calls.asStateFlow()

    @Inject lateinit var callAudios: CallAudiosInteractor
    @Inject lateinit var callsRepository: CallsRepository
    @Inject lateinit var callsInteractor: CallsInteractor
    @Inject lateinit var preferences: PreferencesInteractor
    @Inject lateinit var callNotification: CallNotification


    override fun onCreate() {
        super.onCreate()
        sInstance = this
        callNotification.attach()
    }

    override fun onDestroy() {
        callNotification.detach()
        super.onDestroy()
    }

    override fun onCallAdded(telecomCall: android.telecom.Call) {
        super.onCallAdded(telecomCall)
        val call = Call(telecomCall)
        addCall(call)
        callsInteractor.entryAddCall(call)
        if (!sIsActivityActive && (preferences.incomingCallMode == IncomingCallMode.FULL_SCREEN || call.isDirectionOutgoing)) {
            startCallActivity()
        }
    }


    override fun onCallRemoved(telecomCall: android.telecom.Call) {
        super.onCallRemoved(telecomCall)
        removeCall(Call(telecomCall))
        callsInteractor.getCallByTelecomCall(telecomCall)
            ?.let(callsInteractor::entryRemoveCall)
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        callAudios.entryCallAudioStateChanged(callAudioState)
    }

    private fun startCallActivity() {
        val intent = Intent(this, CallActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun addCall(call: Call) {
        val list = calls.value.toMutableList()
        list.add(call)
        _calls.value = list
    }

    private fun removeCall(call: Call) {
        val list = calls.value.toMutableList()
        list.remove(call)
        _calls.value = list
    }


    companion object {
        var sIsActivityActive = false
        var sInstance: CallService? = null
    }
}