package com.chooloo.www.chooloolib.interactor.sim

import android.telecom.PhoneAccount
import android.telephony.SubscriptionInfo
import com.chooloo.www.chooloolib.data.model.SimAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface SimsInteractor : BaseInteractor<SimsInteractor.Listener> {
    interface Listener

    suspend fun getIsMultiSim(): Boolean
    suspend fun getSimAccounts(): List<SimAccount>
    suspend fun getPhoneAccounts(): List<PhoneAccount>
    suspend fun getSubscriptionInfos(): List<SubscriptionInfo>
}