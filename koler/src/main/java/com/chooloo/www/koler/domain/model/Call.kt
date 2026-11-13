package com.chooloo.www.koler.domain.model

import com.chooloo.www.koler.domain.enum.CallCapability
import com.chooloo.www.koler.domain.enum.CallState

data class Call(
    val id: String,
    val state: CallState,
    val number: String?,
    val displayName: String? = null,
    val isConference: Boolean = false,
    val connectTimeMillis: Long = 0,
    val capabilities: Set<CallCapability> = emptySet(),
    val phoneAccounts: List<PhoneAccount> = emptyList(),
) {
    val hasStarted: Boolean
        get() = connectTimeMillis > 0 && state !in NOT_STARTED_STATES

    fun can(capability: CallCapability) = capability in capabilities

    private companion object {
        val NOT_STARTED_STATES = setOf(
            CallState.DIALING,
            CallState.INCOMING,
            CallState.CONNECTING,
            CallState.SELECT_PHONE_ACCOUNT,
        )
    }
}
