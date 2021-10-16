package com.chooloo.www.koler.interactor.sim

import android.telecom.PhoneAccount
import android.telephony.SubscriptionInfo
import com.chooloo.www.koler.data.account.SimAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface SimInteractor : BaseInteractor<SimInteractor.Listener> {
    interface Listener

    fun askForSim(callback: (SimAccount?) -> Unit)

    fun getIsMultiSim(callback: (Boolean) -> Unit)
    fun getSimAccounts(callback: (List<SimAccount>) -> Unit)
    fun getPhoneAccounts(callback: (List<PhoneAccount>) -> Unit)
    fun getSubscriptionInfos(callback: (List<SubscriptionInfo>) -> Unit)
}