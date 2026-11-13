package com.chooloo.www.chooloolib.data.repository

import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Singleton
class TelecomRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val telecomManager: TelecomManager,
    private val telephonyManager: TelephonyManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TelecomRepository {
    override fun isDefaultDialer(): Boolean =
        context.packageName == telecomManager.defaultDialerPackage

    @RequiresPermission(CALL_PHONE)
    override suspend fun callNumber(number: String) {
        val scheme = if (number.contains("@") || number.contains("%40")) {
            PhoneAccount.SCHEME_SIP
        } else {
            PhoneAccount.SCHEME_TEL
        }
        telecomManager.placeCall(Uri.fromParts(scheme, number, null), Bundle())
    }

    @RequiresPermission(CALL_PHONE)
    override suspend fun callVoicemail() {
        telecomManager.placeCall(Uri.fromParts(PhoneAccount.SCHEME_VOICEMAIL, "", null), Bundle())
    }

    override suspend fun handleSecretCode(code: String) {
        if (code.length > 8 && code.startsWith("*#*#") && code.endsWith("#*#*")) {
            telephonyManager.sendDialerSpecialCode(code.substring(4, code.length - 4))
        }
    }

    // Telecom also allows the default dialer, which the annotation on this API does not list.
    @SuppressLint("MissingPermission")
    override suspend fun cancelMissedCallsNotification() = withContext(ioDispatcher) {
        if (isDefaultDialer()) telecomManager.cancelMissedCallsNotification()
    }
}
