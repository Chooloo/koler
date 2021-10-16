package com.chooloo.www.koler.data.account

import android.net.Uri
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle

data class SimAccount(
    val index: Int,
    val phoneAccount: PhoneAccount
) {
    val label: String
    val address: String
    val phoneAccountHandle: PhoneAccountHandle get() = phoneAccount.accountHandle

    init {
        var raw_label = phoneAccount.label.toString()
        var raw_address = phoneAccount.address.toString()
        if (raw_address.startsWith("tel:") && raw_address.substring(4).isNotEmpty()) {
            raw_address = Uri.encode(raw_address.substring(4))
            raw_label = "${phoneAccount.label}(${raw_address})"
        }
        label = raw_label
        address = raw_address
    }
}