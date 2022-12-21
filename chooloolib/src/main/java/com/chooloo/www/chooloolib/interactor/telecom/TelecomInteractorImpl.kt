package com.chooloo.www.chooloolib.interactor.telecom

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
import com.chooloo.www.chooloolib.data.model.SimAccount
import com.chooloo.www.chooloolib.util.CallUtils
import com.chooloo.www.chooloolib.util.PhoneNumberUtils
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelecomInteractorImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    private val telephonyManager: TelephonyManager,
    @ApplicationContext private val context: Context
) : BaseObservable<TelecomInteractor.Listener>(), TelecomInteractor {
    private val SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE"

    override fun handleMmi(code: String) = telecomManager.handleMmi(code)

    override fun handleSecretCode(code: String): Boolean {
        if (!PhoneNumberUtils.isSecretCode(code)) {
            return false;
        }

        val secretCode: String = code.substring(4, code.length - 4)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.sendDialerSpecialCode(secretCode)
        } else {
            // System service call is not supported pre-O, so must use a broadcast for N-.
            val intent = Intent(SECRET_CODE_ACTION, Uri.parse("android_secret_code://$secretCode"))
            context.sendBroadcast(intent)
        }
        return true;
    }

    override fun handleSpecialChars(code: String) = handleMmi(code) || handleSecretCode(code)

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    override fun callVoicemail() {
        telecomManager.placeCall(Uri.fromParts(PhoneAccount.SCHEME_VOICEMAIL, "", null), Bundle())
    }

    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE])
    override fun callNumber(number: String, simAccount: SimAccount?) {
        val extras = Bundle()
        simAccount?.phoneAccountHandle?.let {
            extras.putParcelable(VoicemailContract.EXTRA_PHONE_ACCOUNT_HANDLE, it)
        }
        telecomManager.placeCall(CallUtils.getCallURI(number), extras)
    }
}