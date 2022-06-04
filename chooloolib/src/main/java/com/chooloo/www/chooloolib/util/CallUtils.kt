package com.chooloo.www.chooloolib.util

import android.net.Uri
import android.telecom.PhoneAccount

object CallUtils {
    fun getCallURI(number: String) = if (PhoneNumberUtils.isUri(number)) {
        Uri.fromParts(PhoneAccount.SCHEME_SIP, number, null);
    } else {
        Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null);
    }
}