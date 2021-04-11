package com.chooloo.www.koler.util.call

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telecom.Call
import android.telecom.PhoneAccountHandle
import android.telephony.PhoneNumberUtils
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.permissions.hasSelfPermission
import com.chooloo.www.koler.util.permissions.isDefaultDialer
import com.chooloo.www.koler.util.permissions.requestDefaultDialer


//region call functions
fun Call.getNumber() = details?.gatewayInfo?.originalAddress?.schemeSpecificPart
    ?: details?.handle?.schemeSpecificPart

fun Call.getValidE164Number(context: Context): String =
    PhoneNumberUtils.formatNumberToE164(getNumber(), context.resources.configuration.locale.country)

fun Call.getNormalizedNumber(context: Context): String =
    PhoneNumberUtils.normalizeNumber(getValidE164Number(context))

fun Call.lookupContact(context: Context) =
    context.lookupContact(getNumber())
//endregion

//region activity related functions
fun BaseActivity.call(number: String) {
    if (isDefaultDialer()) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${Uri.encode(number)}")
        startActivity(callIntent)
    } else {
        requestDefaultDialer()
        showError("Set Koler as your default dialer to make calls")
    }
}

fun BaseActivity.callVoicemail() {
    try {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("voicemail:1"))
        startActivity(intent)
    } catch (e: SecurityException) {
        showError("Couldn't start voicemail, no permissions")
    }
}

fun isUSSDCode(string: String) = string.startsWith("*") && string.endsWith("#")

@SuppressLint("MissingPermission")
fun Activity.getSubscriptionInfo(phoneAccountHandle: PhoneAccountHandle): SubscriptionInfo? {
    if (phoneAccountHandle.id.isEmpty() || hasSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
        return null
    }
    getSystemService(SubscriptionManager::class.java).activeSubscriptionInfoList.forEach {
        if (phoneAccountHandle.id.startsWith(it.iccId)) {
            return it
        }
    }
    return null
}
//endregion