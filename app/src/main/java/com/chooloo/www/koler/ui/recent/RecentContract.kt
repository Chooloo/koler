package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.BaseContract

interface RecentContract : BaseContract {
    interface View : BaseContract.View {
        fun smsRecent()
        fun addContact()
        fun callRecent()
        fun openContact()
        fun openHistory()
        fun blockNumber()
        fun deleteRecent()
        fun unblockNumber()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onActionSms()
        fun onActionCall()
        fun onActionDelete()
        fun onActionAddContact()
        fun onActionOpenContact()
        fun onActionShowHistory()
        fun onActionBlockNumber()
        fun onActionUnblockNumber()
    }
}