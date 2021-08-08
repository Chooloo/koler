package com.chooloo.www.koler.util.call

import ContactsManager
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.telecom.Call
import android.telecom.PhoneAccountHandle
import android.telephony.PhoneNumberUtils
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.util.permissions.PermissionsManager


//region call functions
fun Call.getNumber() = details?.gatewayInfo?.originalAddress?.schemeSpecificPart
    ?: details?.handle?.schemeSpecificPart

fun Call.getValidE164Number(context: Context): String =
    PhoneNumberUtils.formatNumberToE164(getNumber(), context.resources.configuration.locale.country)

fun Call.getNormalizedNumber(context: Context): String =
    PhoneNumberUtils.normalizeNumber(getValidE164Number(context))

fun Call.lookupContact(context: Context) =
    ContactsManager(context).lookupAccountByNumber(getNumber())
//endregion


fun isUSSDCode(string: String) = string.startsWith("*") && string.endsWith("#")

@SuppressLint("MissingPermission")
fun BaseActivity.getSubscriptionInfo(phoneAccountHandle: PhoneAccountHandle): SubscriptionInfo? {
    val permissionsManager = PermissionsManager(this)
    if (phoneAccountHandle.id.isEmpty() || permissionsManager.hasSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
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