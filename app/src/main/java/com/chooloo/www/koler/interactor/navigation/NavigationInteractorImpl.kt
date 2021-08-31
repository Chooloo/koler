package com.chooloo.www.koler.interactor.navigation

import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.provider.VoicemailContract.EXTRA_PHONE_ACCOUNT_HANDLE
import android.telecom.TelecomManager
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.SimAccount
import com.chooloo.www.koler.interactor.permission.PermissionInteractor
import com.chooloo.www.koler.interactor.sim.SimInteractor
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.main.MainActivity
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class NavigationInteractorImpl(
    private val activity: BaseActivity,
    private val simInteractor: SimInteractor,
    private val telecomManager: TelecomManager,
    private val stringInteractor: StringInteractor,
    private val permissionInteractor: PermissionInteractor
) :
    BaseObservable<NavigationInteractor.Listener>(),
    NavigationInteractor {

    override fun goToRateApp() {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=" + activity.application.packageName)
        activity.startActivity(intent)
    }

    override fun goToSendEmail() {
        activity.startActivity(Intent(ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(EXTRA_EMAIL, arrayOf(stringInteractor.getString(R.string.support_email)))
        })
    }

    override fun goToAppGithub() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(stringInteractor.getString(R.string.app_source_url))
            )
        )
    }

    override fun goToReportBugPage() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(stringInteractor.getString(R.string.app_bug_reporting_url))
            )
        )
    }

    override fun goToDonatePage() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(stringInteractor.getString(R.string.donation_link))
            )
        )
    }

    override fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    override fun goToManageBlockedNumbers() {
        activity.startActivity(telecomManager.createManageBlockedNumbersIntent(), null)
    }


    override fun callVoicemail() {
        permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_call) {
            val intent = Intent(ACTION_CALL)
            intent.data = Uri.fromParts("voicemail", "", null)
            activity.startActivity(intent)
        }
    }

    override fun call(number: String) {
        permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_call, {
            simInteractor.getIsMultiSim { isMultiSim ->
                if (isMultiSim) {
                    simInteractor.askForSim { call(it, number) }
                } else {
                    simInteractor.getSimAccounts { call(it[0], number) }
                }
            }
        }, {
            call(null, number)
        })
    }

    @SuppressLint("MissingPermission")
    override fun call(simAccount: SimAccount?, number: String) {
        permissionInteractor.runWithPermissions(arrayOf(CALL_PHONE), {
            val extras = Bundle()
            simAccount?.phoneAccountHandle?.let {
                extras.putParcelable(EXTRA_PHONE_ACCOUNT_HANDLE, it)
            }
            telecomManager.placeCall(Uri.parse("tel:${Uri.encode(number)}"), extras)
        }, {
            activity.showError(R.string.error_no_permissions_make_call)
        }, {
            activity.showError(R.string.error_no_permissions_make_call)
        })
    }
}