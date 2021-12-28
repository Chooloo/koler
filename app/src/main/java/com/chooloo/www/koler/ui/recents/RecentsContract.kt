package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.data.account.RecentAccount
import com.chooloo.www.koler.ui.list.ListContract

interface RecentsContract : ListContract {
    interface View : ListContract.View<RecentAccount> {
        fun openRecent(recent: RecentAccount)
    }

    interface Controller<V : View> : ListContract.Controller<RecentAccount, V> {

    }
}