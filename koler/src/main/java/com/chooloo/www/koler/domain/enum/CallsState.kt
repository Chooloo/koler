package com.chooloo.www.koler.domain.enum

enum class CallsState(val showsActions: Boolean, val isEnded: Boolean) {
    NO_CALLS(showsActions = false, isEnded = true),
    INCOMING(showsActions = false, isEnded = false),
    WAITING_FOR_ACCOUNT(showsActions = false, isEnded = false),
    OUTGOING(showsActions = true, isEnded = false),
    ACTIVE(showsActions = true, isEnded = false),
    ACTIVE_MULTI(showsActions = true, isEnded = false),
    DISCONNECTED(showsActions = false, isEnded = true),
}
