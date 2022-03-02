package com.chooloo.www.chooloolib.model

import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import com.chooloo.www.chooloolib.util.fullAddress
import com.chooloo.www.chooloolib.util.fullLabel

data class SimAccount(
    val index: Int,
    val phoneAccount: PhoneAccount
) {
    val label: String
    val address: String
    val phoneAccountHandle: PhoneAccountHandle get() = phoneAccount.accountHandle

    init {
        label = phoneAccount.fullLabel()
        address = phoneAccount.fullAddress()
    }
}