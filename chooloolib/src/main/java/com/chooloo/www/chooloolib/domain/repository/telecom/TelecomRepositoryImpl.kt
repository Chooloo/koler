package com.chooloo.www.chooloolib.domain.repository.telecom

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.VoicemailContract
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.domain.model.record.SimRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.utils.PhoneNumberUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelecomRepositoryImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    private val telephonyManager: TelephonyManager,
    @ApplicationContext private val context: Context
) : BaseRepositoryImpl(), TelecomRepository {
    companion object {
        private const val SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE"
    }

    override suspend fun getCallUri(number: String): Uri =
        Uri.fromParts(
            if (PhoneNumberUtils.isUri(number)) PhoneAccount.SCHEME_SIP else PhoneAccount.SCHEME_TEL,
            number,
            null
        )

    override suspend fun handleMmi(code: String) = telecomManager.handleMmi(code)

    override suspend fun handleSecretCode(code: String): Boolean {
        if (!PhoneNumberUtils.isSecretCode(code)) {
            return false
        }

        val secretCode: String = code.substring(4, code.length - 4)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.sendDialerSpecialCode(secretCode)
        } else {
            // System service call is not supported pre-O, so must use a broadcast for N-.
            val intent = Intent(SECRET_CODE_ACTION, Uri.parse("android_secret_code://$secretCode"))
            context.sendBroadcast(intent)
        }
        return true
    }

    override suspend fun handleSpecialChars(code: String) =
        handleSecretCode(code)
//        handleMmi(code) || handleSecretCode(code)

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    override suspend fun callVoicemail() {
        telecomManager.placeCall(Uri.fromParts(PhoneAccount.SCHEME_VOICEMAIL, "", null), Bundle())
    }

    override suspend fun isCallEmergency(call: CallData) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            telephonyManager.isEmergencyNumber(call.number ?: "")
        } else {
            @Suppress("DEPRECATION")
            android.telephony.PhoneNumberUtils.isEmergencyNumber(call.number ?: "")
        }

    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE])
    override suspend fun callNumber(number: String, simRecord: SimRecord?) {
        val extras = Bundle()
        simRecord?.phoneAccountHandle?.let {
            extras.putParcelable(VoicemailContract.EXTRA_PHONE_ACCOUNT_HANDLE, it)
        }
        telecomManager.placeCall(getCallUri(number), extras)
    }
}