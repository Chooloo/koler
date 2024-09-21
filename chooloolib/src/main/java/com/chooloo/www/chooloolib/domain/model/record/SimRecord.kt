package com.chooloo.www.chooloolib.domain.model.record

import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import com.chooloo.www.chooloolib.utils.fullAddress
import com.chooloo.www.chooloolib.utils.fullLabel

data class SimRecord(
    val index: Int,
    val phoneAccount: PhoneAccount
) {
    val phoneAccountHandle: PhoneAccountHandle get() = phoneAccount.accountHandle
    val label: String = phoneAccount.fullLabel()
    val address: String = phoneAccount.fullAddress()
}