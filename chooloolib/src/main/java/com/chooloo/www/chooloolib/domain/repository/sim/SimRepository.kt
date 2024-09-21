package com.chooloo.www.chooloolib.domain.repository.sim

import android.telecom.PhoneAccount
import android.telephony.SubscriptionInfo
import com.chooloo.www.chooloolib.domain.model.record.SimRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface SimRepository : BaseRepository {
    suspend fun getIsMultiSim(): Boolean
    suspend fun getSimAccounts(): List<SimRecord>
    suspend fun getPhoneAccounts(): List<PhoneAccount>
    suspend fun getSubscriptionInfos(): List<SubscriptionInfo>
}