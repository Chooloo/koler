package com.chooloo.www.koler.domain

data class Call(
    val id: String,
    val state: CallState,
    val number: String?,
    val displayName: String? = null,
    val isConference: Boolean = false,
    val connectTimeMillis: Long = 0,
    val capabilities: Set<Capability> = emptySet(),
) {
    enum class Capability { MUTE, HOLD, SWAP, MERGE, SEPARATE }

    val hasStarted: Boolean
        get() = connectTimeMillis > 0 && state !in NOT_STARTED_STATES

    fun can(capability: Capability) = capability in capabilities

    private companion object {
        val NOT_STARTED_STATES = setOf(
            CallState.DIALING,
            CallState.INCOMING,
            CallState.CONNECTING,
            CallState.SELECT_PHONE_ACCOUNT,
        )
    }
}
