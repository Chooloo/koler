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
        var rawLabel = phoneAccount.label.toString()
        var rawAddress = phoneAccount.address.toString()
        if (rawAddress.startsWith("tel:") && rawAddress.substring(4).isNotEmpty()) {
            rawAddress = Uri.encode(rawAddress.substring(4))
            rawLabel = "${phoneAccount.label}(${rawAddress})"
        }
        label = rawLabel
        address = rawAddress
    }
}