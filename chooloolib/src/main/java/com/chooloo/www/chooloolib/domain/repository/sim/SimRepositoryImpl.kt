package com.chooloo.www.chooloolib.domain.repository.sim

import android.Manifest.permission.READ_PHONE_STATE
import android.telecom.PhoneAccount
import android.telecom.TelecomManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.domain.model.record.SimRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimRepositoryImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    private val subscriptionManager: SubscriptionManager,
) : BaseRepositoryImpl(), SimRepository {
    @RequiresPermission(READ_PHONE_STATE)
    override suspend fun getIsMultiSim(): Boolean =
        telecomManager.callCapablePhoneAccounts.size > 1
    
    @RequiresPermission(READ_PHONE_STATE)
    override suspend fun getSimAccounts(): List<SimRecord> =
        getPhoneAccounts().mapIndexed(::SimRecord)

    @RequiresPermission(READ_PHONE_STATE)
    override suspend fun getPhoneAccounts(): List<PhoneAccount> =
        telecomManager.callCapablePhoneAccounts.map(telecomManager::getPhoneAccount)

    @RequiresPermission(READ_PHONE_STATE)
    override suspend fun getSubscriptionInfos(): List<SubscriptionInfo> =
        subscriptionManager.activeSubscriptionInfoList
}