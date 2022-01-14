package com.chooloo.www.chooloolib.interactor.recents

import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface RecentsInteractor : BaseInteractor<RecentsInteractor.Listener> {
    interface Listener

    fun deleteRecent(recentId: Long)
    fun queryRecent(recentId: Long): RecentAccount?
    fun queryRecent(recentId: Long, callback: (RecentAccount?) -> Unit)
    fun getCallTypeImage(@RecentAccount.CallType callType: Int): Int
    fun getLastOutgoingCall(): String
}