package com.chooloo.www.koler.domain

enum class CallState {
    CONNECTING,
    DIALING,
    INCOMING,
    ACTIVE,
    HOLDING,
    CONFERENCED,
    DISCONNECTING,
    DISCONNECTED,
    SELECT_PHONE_ACCOUNT,
    UNKNOWN,
}
