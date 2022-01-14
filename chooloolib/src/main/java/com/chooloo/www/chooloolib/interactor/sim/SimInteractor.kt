package com.chooloo.www.chooloolib.interactor.sim

import android.telecom.PhoneAccount
import android.telephony.SubscriptionInfo
import com.chooloo.www.chooloolib.data.account.SimAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface SimInteractor : BaseInteractor<SimInteractor.Listener> {
    interface Listener

    fun getIsMultiSim(callback: (Boolean) -> Unit)
    fun getSimAccounts(callback: (List<SimAccount>) -> Unit)
    fun getPhoneAccounts(callback: (List<PhoneAccount>) -> Unit)
    fun getSubscriptionInfos(callback: (List<SubscriptionInfo>) -> Unit)
}