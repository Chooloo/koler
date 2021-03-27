package com.chooloo.www.koler.util.call

import android.content.Context
import android.telecom.Call
import android.telephony.PhoneNumberUtils
import com.chooloo.www.koler.util.lookupContact

fun Call.getNumber() = details?.gatewayInfo?.originalAddress?.schemeSpecificPart
    ?: details?.handle?.schemeSpecificPart

fun Call.getValidE164Number(context: Context) =
    PhoneNumberUtils.formatNumberToE164(getNumber(), context.resources.configuration.locale.country)

fun Call.getNormalizedNumber(context: Context) =
    PhoneNumberUtils.normalizeNumber(getValidE164Number(context))

fun Call.getCallerContact(context: Context) =
    context.lookupContact(getNumber())