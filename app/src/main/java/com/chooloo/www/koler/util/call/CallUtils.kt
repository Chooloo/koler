package com.chooloo.www.koler.util.call

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.telecom.PhoneAccountHandle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.permissions.hasSelfPermission
import com.chooloo.www.koler.util.permissions.isDefaultDialer
import com.chooloo.www.koler.util.permissions.requestDefaultDialer

fun BaseActivity.call(number: String) {
    if (isDefaultDialer()) {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(number)))
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

@SuppressLint("MissingPermission")
fun Activity.getSubscriptionInfo(phoneAccountHandle: PhoneAccountHandle): SubscriptionInfo? {
    if (phoneAccountHandle.id.isEmpty() || hasSelfPermission(READ_PHONE_STATE)) {
        return null
    }
    getSystemService(SubscriptionManager::class.java).activeSubscriptionInfoList.forEach {
        if (phoneAccountHandle.id.startsWith(it.iccId)) {
            return it
        }
    }
    return null
}