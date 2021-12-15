package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import java.util.*

abstract class PhoneCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.action == CallRecordReceiver.ACTION_OUT) {
            savedNumber = intent.extras!!.getString(CallRecordReceiver.EXTRA_PHONE_NUMBER)
        } else {
            savedNumber = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val state = when (intent.extras!!.getString(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_IDLE -> TelephonyManager.CALL_STATE_IDLE
                TelephonyManager.EXTRA_STATE_OFFHOOK -> TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING -> TelephonyManager.CALL_STATE_RINGING
                else -> 0
            }
            onCallStateChanged(context, state, savedNumber)
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected abstract fun onMissedCall(context: Context, number: String?, start: Date)
    protected abstract fun onOutgoingCallStarted(context: Context, number: String?, start: Date)
    protected abstract fun onIncomingCallReceived(context: Context, number: String?, start: Date)
    protected abstract fun onIncomingCallAnswered(context: Context, number: String?, start: Date)
    protected abstract fun onIncomingCallEnded(
        context: Context,
        number: String?,
        start: Date,
        end: Date
    )

    protected abstract fun onOutgoingCallEnded(
        context: Context,
        number: String?,
        start: Date,
        end: Date
    )

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    fun onCallStateChanged(context: Context, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                savedNumber = number

                onIncomingCallReceived(context, number, callStartTime)
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()

                    onOutgoingCallStarted(context, savedNumber, callStartTime)
                } else {
                    isIncoming = true
                    callStartTime = Date()

                    onIncomingCallAnswered(context, savedNumber, callStartTime)
                }
            TelephonyManager.CALL_STATE_IDLE ->
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                when {
                    lastState == TelephonyManager.CALL_STATE_RINGING -> onMissedCall(
                        context,
                        savedNumber,
                        callStartTime
                    )
                    isIncoming -> onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                    else -> onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
        }
        lastState = state
    }


    companion object {
        private var isIncoming: Boolean = false
        private var savedNumber: String? = null
        private var callStartTime: Date = Date()
        private var lastState = TelephonyManager.CALL_STATE_IDLE
    }
}