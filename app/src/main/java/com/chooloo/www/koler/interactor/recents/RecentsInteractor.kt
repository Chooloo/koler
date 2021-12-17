package com.chooloo.www.koler.interactor.recents

import com.chooloo.www.koler.data.account.RecentAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface RecentsInteractor : BaseInteractor<RecentsInteractor.Listener> {
    interface Listener

    fun deleteRecent(recentId: Long)
    fun queryRecent(recentId: Long): RecentAccount?
    fun queryRecent(recentId: Long, callback: (RecentAccount?) -> Unit)
    fun getCallTypeImage(@RecentAccount.CallType callType: Int): Int
    fun getLastOutgoingCall(): String
}