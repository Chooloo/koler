package com.chooloo.www.chooloolib.interactor.sim

import android.Manifest.permission.READ_PHONE_STATE
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.model.SimAccount
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimsInteractorImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    private val subscriptionManager: SubscriptionManager,
) : BaseObservable<SimsInteractor.Listener>(), SimsInteractor {

    @RequiresPermission(READ_PHONE_STATE)
    override fun getIsMultiSim(callback: (isMultiSim: Boolean) -> Unit) {
        callback.invoke(telecomManager.callCapablePhoneAccounts.size > 1)
    }

    @RequiresPermission(READ_PHONE_STATE)
    override fun getSimAccounts(callback: (List<SimAccount>) -> Unit) {
        getPhoneAccounts {
            callback.invoke(it.mapIndexed { index, phoneAccount ->
                SimAccount(index, phoneAccount)
            })
        }
    }

    @RequiresPermission(READ_PHONE_STATE)
    override fun getPhoneAccounts(callback: (List<PhoneAccount>) -> Unit) {
        val phoneAccounts = ArrayList<PhoneAccount>()
        telecomManager.callCapablePhoneAccounts.forEach { pah ->
            phoneAccounts.add(telecomManager.getPhoneAccount(pah))
        }
        callback.invoke(phoneAccounts)
    }

    @RequiresPermission(READ_PHONE_STATE)
    override fun getSubscriptionInfos(callback: (List<SubscriptionInfo>) -> Unit) {
        callback.invoke(subscriptionManager.activeSubscriptionInfoList)
    }
}