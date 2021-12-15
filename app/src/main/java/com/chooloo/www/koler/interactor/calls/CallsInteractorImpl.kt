package com.chooloo.www.koler.interactor.calls

import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.data.call.Call.State.*
import com.chooloo.www.koler.data.call.CallList
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class CallsInteractorImpl : BaseObservable<CallsInteractor.Listener>(), CallsInteractor {
    private val _callList: CallList = CallList()

    override val mainCall: Call?
        get() {
            val mainCall = if (_callList.size == 1) {
                _callList.getIndex(0)
            } else {
                _callList.getFirstState(DIALING)
                    ?: _callList.getFirstState(INCOMING)
                    ?: _callList.getFirstState(ACTIVE)
                    ?: _callList.getFirstState(HOLDING)
            }
            return mainCall?.parentCall ?: mainCall
        }

    override val callsCount: Int
        get() = _callList.size

    override val isMultiCall: Boolean
        get() = _callList.conferenceCalls.size + _callList.nonConferenceCalls.size > 1


    override fun getStateCount(state: Call.State) =
        _callList.getState(state).size

    override fun getFirstState(state: Call.State) =
        _callList.getFirstState(state)

    override fun getCallByTelecomCall(telecomCall: android.telecom.Call) =
        _callList.getByTelecomCall(telecomCall)


    override fun swapCall(callId: String) {
        _callList[callId]?.swapConference()
    }

    override fun mergeCall(callId: String) {
        _callList[callId]?.merge()
    }

    override fun holdCall(callId: String) {
        _callList[callId]?.hold()
    }

    override fun unHoldCall(callId: String) {
        _callList[callId]?.unHold()
    }

    override fun toggleHold(callId: String) {
        _callList[callId]?.let {
            if (it.isHolding) {
                it.unHold()
            } else {
                it.hold()
            }
        }
    }

    override fun answerCall(callId: String) {
        _callList[callId]?.answer()
    }

    override fun rejectCall(callId: String) {
        _callList[callId]?.reject()
    }

    override fun invokeCallKey(callId: String, c: Char) {
        _callList[callId]?.invokeKey(c)
    }

    @Synchronized
    override fun onCallChanged(call: Call) {
        invokeListeners { l -> l.onCallChanged(call) }

        val mainCall = mainCall
        if (mainCall == null || mainCall == call) {
            invokeListeners { l -> l.onMainCallChanged(call) }
        }
    }


    override fun entryAddCall(call: Call) {
        _callList.update(call)
        call.registerListener(this)
        onCallChanged(call)
    }

    override fun entryRemoveCall(call: Call) {
        _callList.remove(call)
        call.unregisterListener(this)
        if (_callList.size == 0) {
            listeners.forEach { l -> l.onNoCalls() }
        }
    }


    companion object {
        val instance by lazy { CallsInteractorImpl() }
    }
}