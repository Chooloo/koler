package com.chooloo.www.koler.ui.call

enum class CallActionType { KEYPAD, MUTE, HOLD, ADD_CALL, SPEAKER, SWAP, MERGE, ACCEPT, DENY, MANAGE }

data class CallAction(
    val type: CallActionType,
    val isEnabled: Boolean,
    val isActive: Boolean = false,
)
