package com.chooloo.www.chooloolib.ui.recents

import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.ui.list.ListContract

interface RecentsContract : ListContract {
    interface View : ListContract.View<RecentAccount> {
    }

    interface Controller : ListContract.Controller<RecentAccount, View> {
    }
}