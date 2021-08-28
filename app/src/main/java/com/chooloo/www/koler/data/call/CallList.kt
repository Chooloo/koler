package com.chooloo.www.koler.data.call

import java.util.*

class CallList {
    private var _callsById: HashMap<String, Call> = HashMap()

    val size: Int
        get() = _callsById.size

    val calls: List<Call>
        get() = ArrayList(_callsById.values)

    val conferenceCalls
        get() = calls.filter(Call::isConference)

    val nonConferenceCalls
        get() = calls.filter { call -> !call.isInConference && !call.isConference }

    val conferenceableCalls
        get() = mutableListOf<Call>().apply {
            addAll(calls.filter(Call::isConferenceParent))
            addAll(calls.filter(Call::isHolding))
            addAll(calls.filter(Call::isActive))
        }

    val conferenceParentCalls
        get() = calls.filter(Call::isConferenceParent)


    operator fun get(callId: String) =
        _callsById.get(callId)

    operator fun contains(call: Call) =
        _callsById.containsKey(call.id)


    fun getIndex(index: Int): Call? =
        ArrayList(_callsById.values).get(index)

    fun hasState(state: Call.State) =
        calls.any { call -> call.state == state }

    fun getState(state: Call.State) =
        calls.filter { call -> call.state == state }

    fun getFirstState(state: Call.State) =
        calls.firstOrNull { call -> call.state == state }

    fun hasStates(states: Array<Call.State>) =
        states.all(this::hasState)

    fun getNotStates(states: Array<Call.State>) =
        calls.filter { call -> states.none { state -> state == call.state } }

    fun getByTelecomCall(telecomCall: android.telecom.Call) =
        calls.firstOrNull { call -> call.telecomCall == telecomCall }


    @Synchronized
    fun update(call: Call) {
        _callsById[call.id] = call
    }

    /**
     * Try to remove a given call from hash map values
     *
     *
     * If no call exist with the same id, try to remove by telecom call
     * In case of onCallRemoved entry, a call that's already exists will be created with a new id
     *
     *
     * @param call Koler call item to remove
     */
    @Synchronized
    fun remove(call: Call) {
        if (_callsById.containsKey(call.id)) {
            _callsById.remove(call.id)
        } else {
            calls.filter { c -> c == call }.forEach(_callsById.values::remove)
        }
    }
}