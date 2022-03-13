package com.chooloo.www.chooloolib.interactor.call

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.provider.VoicemailContract
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.model.SimAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CallNavigationsInteractorImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    private val permissions: PermissionsInteractor,
    @ApplicationContext private val context: Context
) : BaseInteractorImpl<CallNavigationsInteractor.Listener>(), CallNavigationsInteractor {
    override fun callVoicemail() {
        permissions.runWithDefaultDialer(R.string.error_not_default_dialer_call) {
            val intent = Intent(Intent.ACTION_CALL)
                .setData(Uri.fromParts(PhoneAccount.SCHEME_VOICEMAIL, "", null))
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE])
    override fun call(number: String) {
        telecomManager.placeCall(Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null), Bundle())
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    override fun call(simAccount: SimAccount?, number: String) {
        val extras = Bundle()
        simAccount?.phoneAccountHandle?.let {
            extras.putParcelable(VoicemailContract.EXTRA_PHONE_ACCOUNT_HANDLE, it)
        }
        telecomManager.placeCall(Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null), extras)
    }
}
