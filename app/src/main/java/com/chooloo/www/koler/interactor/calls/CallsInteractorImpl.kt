package com.chooloo.www.koler.interactor.calls

import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.data.call.Call.State.*
import com.chooloo.www.koler.data.call.CallList
import com.chooloo.www.koler.util.BaseObservable

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
            return if (mainCall?.isInConference == true) {
                mainCall.parentCall
            } else {
                mainCall
            }
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


    override fun entryAddCall(call: Call) {
        _callList.update(call)
        call.registerListener(this)
        onCallChanged(call)
    }

    override fun entryRemoveCall(call: Call) {
        _callList.remove(call)
        call.unregisterListener(this)
        if (_callList.size == 0) {
            invokeListeners(CallsInteractor.Listener::onNoCalls)
        }
    }


    override fun swapCall(callId: String) {
        _callList.get(callId)?.swapConference()
    }

    override fun mergeCall(callId: String) {
        _callList.get(callId)?.merge()
    }

    override fun holdCall(callId: String) {
        _callList.get(callId)?.hold()
    }

    override fun unHoldCall(callId: String) {
        _callList.get(callId)?.unHold()
    }

    override fun toggleHold(callId: String) {
        _callList.get(callId)?.let {
            if (it.isHolding) {
                it.hold()
            } else {
                it.unHold()
            }
        }
    }

    override fun answerCall(callId: String) {
        _callList.get(callId)?.answer()
    }

    override fun rejectCall(callId: String) {
        _callList.get(callId)?.reject()
    }

    override fun invokeCallKey(callId: String, c: Char) {
        _callList.get(callId)?.invokeKey(c)
    }


    override fun onCallChanged(call: Call) {
        invokeListeners { l -> l.onCallChanged(call) }

        mainCall.also {
            if (it == null || it == call) {
                invokeListeners { l -> l.onMainCallChanged(call) }
            }
        }
    }
}