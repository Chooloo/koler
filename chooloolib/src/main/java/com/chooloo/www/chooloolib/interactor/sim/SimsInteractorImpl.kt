package com.chooloo.www.chooloolib.interactor.sim

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.account.SimAccount
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SimsInteractorImpl @Inject constructor(
    private val activity: BaseActivity,
    private val telecomManager: TelecomManager,
    private val subscriptionManager: SubscriptionManager,
    private val permissionsInteractor: PermissionsInteractor
) : BaseObservable<SimsInteractor.Listener>(), SimsInteractor {
    @SuppressLint("MissingPermission")
    override fun getIsMultiSim(callback: (isMultiSim: Boolean) -> Unit) {
        permissionsInteractor.runWithPermissions(arrayOf(READ_PHONE_STATE), {
            callback.invoke(telecomManager.callCapablePhoneAccounts.size > 1)
        }, {
            activity.showError(R.string.error_no_permissions_ask_sim)
        })
    }

    override fun getSimAccounts(callback: (List<SimAccount>) -> Unit) {
        getPhoneAccounts {
            callback.invoke(it.mapIndexed { index, phoneAccount ->
                SimAccount(index, phoneAccount)
            })
        }
    }

    @SuppressLint("MissingPermission")
    override fun getPhoneAccounts(callback: (List<PhoneAccount>) -> Unit) {
        permissionsInteractor.runWithPermissions(arrayOf(READ_PHONE_STATE), {
            val phoneAccounts = ArrayList<PhoneAccount>()
            telecomManager.callCapablePhoneAccounts.forEach { pah ->
                phoneAccounts.add(telecomManager.getPhoneAccount(pah))
            }
            callback.invoke(phoneAccounts)
        })
    }

    @SuppressLint("MissingPermission")
    override fun getSubscriptionInfos(callback: (List<SubscriptionInfo>) -> Unit) {
        permissionsInteractor.runWithPermissions(arrayOf(READ_PHONE_STATE), {
            callback.invoke(subscriptionManager.activeSubscriptionInfoList)
        })
    }
}