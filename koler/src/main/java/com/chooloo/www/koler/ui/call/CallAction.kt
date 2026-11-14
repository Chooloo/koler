package com.chooloo.www.koler.ui.call

data class CallAction(
    val type: CallActionType,
    val isEnabled: Boolean,
    val isActive: Boolean = false,
)
