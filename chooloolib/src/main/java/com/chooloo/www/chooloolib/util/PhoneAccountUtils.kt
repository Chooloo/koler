package com.chooloo.www.chooloolib.util

import android.net.Uri
import android.telecom.PhoneAccount

fun PhoneAccount.fullAddress(): String {
    var rawAddress = address.toString()
    if (rawAddress.startsWith("tel:") && rawAddress.substring(4).isNotEmpty()) {
        rawAddress = Uri.encode(rawAddress.substring(4))
    }
    return rawAddress
}

fun PhoneAccount.fullLabel(): String {
    var rawLabel = label.toString()
    var rawAddress = address.toString()
    if (rawAddress.startsWith("tel:") && rawAddress.substring(4).isNotEmpty()) {
        rawAddress = Uri.encode(rawAddress.substring(4))
        rawLabel = "${label}(${rawAddress})"
    }
    return rawLabel
}