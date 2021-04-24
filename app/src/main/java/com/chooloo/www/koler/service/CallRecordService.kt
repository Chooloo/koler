package com.chooloo.www.koler.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.Nullable
import com.chooloo.www.koler.receiver.CallRecordReceiver

open class CallRecordService : Service() {
    private var _callRecordReceiver: CallRecordReceiver? = null

    @Nullable
    override fun onBind(intent: Intent): Nothing? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startCallReceiver()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCallReceiver()
    }

    private fun startCallReceiver() {
        if (_callRecordReceiver == null) {
            _callRecordReceiver = CallRecordReceiver()
        }
        registerReceiver(_callRecordReceiver, IntentFilter().apply {
            addAction(CallRecordReceiver.ACTION_IN)
            addAction(CallRecordReceiver.ACTION_OUT)
        })
    }

    private fun stopCallReceiver() {
        try {
            if (_callRecordReceiver != null) {
                unregisterReceiver(_callRecordReceiver)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}