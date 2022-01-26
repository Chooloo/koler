package com.chooloo.www.chooloolib.interactor.navigation

import android.Manifest
import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.VoicemailContract.EXTRA_PHONE_ACCOUNT_HANDLE
import android.telecom.TelecomManager
import android.telephony.PhoneNumberUtils
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.account.SimAccount
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.sim.SimsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NavigationsInteractorImpl @Inject constructor(
    private val sims: SimsInteractor,
    private val activity: BaseActivity,
    private val dialogs: DialogsInteractor,
    private val strings: StringsInteractor,
    private val telecomManager: TelecomManager,
    private val permissions: PermissionsInteractor,
    private val preferences: PreferencesInteractor,
) :
    BaseObservable<NavigationsInteractor.Listener>(),
    NavigationsInteractor {

    override fun donate() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(strings.getString(R.string.donation_link))
            )
        )
    }

    override fun rateApp() {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=" + activity.application.packageName)
        activity.startActivity(intent)
    }

    override fun sendEmail() {
        activity.startActivity(Intent(ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(EXTRA_EMAIL, arrayOf(strings.getString(R.string.support_email)))
        })
    }

    override fun reportBug() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(strings.getString(R.string.app_bug_reporting_url))
            )
        )
    }

    override fun goToAppGithub() {
        activity.startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse(strings.getString(R.string.app_source_url))
            )
        )
    }

    override fun manageBlockedNumber() {
        activity.startActivity(telecomManager.createManageBlockedNumbersIntent(), null)
    }

    override fun goToLauncherActivity() {
        activity.startActivity(activity.packageManager.getLaunchIntentForPackage(activity.packageName))
    }

    override fun sendSMS(number: String?) {
        val intent =
            Intent(ACTION_SENDTO, Uri.parse("smsto:${PhoneNumberUtils.normalizeNumber(number)}"))
        activity.startActivity(intent)
    }

    override fun addContact(number: String) {
        val intent = Intent(ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        activity.startActivity(intent)
    }

    override fun viewContact(contactId: Long) {
        val intent = Intent(ACTION_VIEW).apply {
            data = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId.toString())
        }
        activity.startActivity(intent)
    }

    override fun goToActivity(activityClass: Class<out BaseActivity>) {
        val intent = Intent(activity, activityClass)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    override fun editContact(contactId: Long) {
        val intent = Intent(ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI).apply {
            data = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        }
        activity.startActivity(intent)
    }


    override fun callVoicemail() {
        permissions.runWithDefaultDialer(R.string.error_not_default_dialer_call) {
            val intent = Intent(ACTION_CALL)
            intent.data = Uri.fromParts("voicemail", "", null)
            activity.startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    override fun call(number: String) {
        activity.component.permissions.runWithPermissions(
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            {
                permissions.runWithDefaultDialer(null, {
                    sims.getIsMultiSim { isMultiSim ->
                        if (preferences.isAskSim && isMultiSim) {
                            dialogs.askForSim { call(it, number) }
                        } else {
                            sims.getSimAccounts { call(it[0], number) }
                        }
                    }
                }, {
                    call(null, number)
                })
            }, {
                activity.showError(R.string.error_no_permissions_ask_sim)
            }, {
                activity.showError(R.string.error_no_permissions_ask_sim)
            }
        )
    }

    @SuppressLint("MissingPermission")
    override fun call(simAccount: SimAccount?, number: String) {
        permissions.runWithPermissions(arrayOf(CALL_PHONE), {
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