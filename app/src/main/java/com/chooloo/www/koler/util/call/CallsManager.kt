package com.chooloo.www.koler.util.call

import android.telecom.Call
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState.DISCONNECTED


object CallsManager : Call.Callback() {
    var sCalls: ArrayList<CallItem> = arrayListOf()
    var sListeners: ArrayList<CallsListener> = arrayListOf()

    interface CallsListener {
        fun onCallChanged(callItem: CallItem) {}
        fun onCallStateChanged(callItem: CallItem) {}
        fun onCallDetailsChanged(callItem: CallItem) {}
    }

    val firstCall: CallItem?
        get() = sCalls.maxByOrNull { it.timeConnected }

    val activeCalls: ArrayList<CallItem>
        get() = ArrayList(sCalls.filter { !it.isDisconnected && it.timeConnected > 0 })

    val incomingCalls: ArrayList<CallItem>
        get() = ArrayList(sCalls.filter { !it.isDisconnected && it.timeConnected == 0L })

    val secondaryCalls: ArrayList<CallItem>
        get() = ArrayList(sCalls.filter { it != firstCall })

    fun addCall(callItem: CallItem) {
        sCalls.add(callItem)
        callItem.registerListener(this)
    }

    fun removeCall(callItem: CallItem) {
        sCalls.remove(callItem)
        callItem.unregisterListener(this)
    }

    fun addCall(call: Call) {
        val callItem = CallItem(call)
        sCalls.add(callItem)
        callItem.registerListener(this)
    }

    fun removeCall(call: Call) {
        val callItem = CallItem(call)
        sCalls.remove(callItem)
        callItem.unregisterListener(this)
    }

    fun registerListener(callsListener: CallsListener) {
        sListeners.add(callsListener)
    }

    fun unregisterListener(callsListener: CallsListener) {
        sListeners.remove(callsListener)
    }

    //region call.callback
    override fun onDetailsChanged(call: Call, details: Call.Details) {
        super.onDetailsChanged(call, details)
        sListeners.forEach {
            it.onCallDetailsChanged(CallItem(call))
            it.onCallChanged(CallItem(call))
        }
    }

    override fun onStateChanged(call: Call, state: Int) {
        super.onStateChanged(call, state)
        val callItem = CallItem(call)
        if (callItem.state == DISCONNECTED) {
            removeCall(callItem)
        }
        sListeners.forEach {
            it.onCallStateChanged(callItem)
            it.onCallChanged(callItem)
        }
    }
    //endregion
}

